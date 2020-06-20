#%%
import pandas as pd
import numpy as np
from os import path
from pandas_profiling import ProfileReport
import matplotlib.pyplot as plt
import seaborn as sns
#%%
base_dir = r"D:\ACoder\AllMyLab\大数据实训\大数据人工智能大作业\house-prices-advanced-regression-techniques"
data_train = pd.read_csv(path.join(base_dir,'train.csv'))
data_test = pd.read_csv(path.join(base_dir,'test.csv'))

#%%
pd.set_option('display.max_columns',None)
print("示例数据：")
print(data_train.head(10))
print("-"*40)

# 数据基本信息
print("数据基本信息:")
print(data_train.info())
print("-"*40)

# 数据分析
print("数据分析:")
print(data_train.describe(include='all'))
print("-"*40)

# 生成详细分析报告
profile = ProfileReport(data_train,title="Pandas Profiling Report")
profile.to_file("analysis.html")

#%%
num = data_train.select_dtypes(exclude='object')
# corr()计算各维度数据的相关性。
numcorr = num.corr()
f,ax = plt.subplots(figsize=(17,1))
sns.heatmap(numcorr.sort_values(by=['SalePrice'],ascending=False).head(1),cmap="Blues")
plt.title(" Numerical features correlation with the sale price", weight='bold', fontsize=18)
plt.xticks(weight='bold')
plt.yticks(weight='bold', color='dodgerblue', rotation=0)
plt.show()

# %%
Num=numcorr['SalePrice'].sort_values(ascending=False).head(10).to_frame()
cm = sns.light_palette("cyan", as_cmap=True)
s = Num.style.background_gradient(cmap=cm)
s



# %%数据清洗=================================================
y_train = data_train['SalePrice'].to_frame()
#将训练集以及测试集合并
# pandas contact 之后，一定要记得用reset_index去处理index,不然容易出现莫名的逻辑错误
# drop=True就是把原来的索引index列去掉，重置index。
data1 = pd.concat((data_train,data_test),sort=False).reset_index(drop=True)
# inplace=True：不创建新的对象，直接对原始对象进行修改；
# inplace=False：对数据进行修改，创建并返回新的对象承载其修改结果。
data1.drop(['SalePrice'],axis=1,inplace=True)
data1.drop(['Id'],axis=1,inplace=True)
print("Total size is : ",data1.shape)

#%%
# 如果缺失值超过百分之20，即不缺失的值未达到80%，删除该特征
data = data1.dropna(thresh=len(data1)*0.8,axis=1)
print(data1.shape[1]-data.shape[1], " features is dropped")
print('The shape of the combined dataset after dropping features with more than 80% M.V.', data.shape)

#%%
# 接下来处理缺失值未超过百分之20的特征
allna = (data.isnull().sum()/len(data))*100
allna = allna.drop(allna[allna == 0].index).sort_values()
NA = data[allna.index.to_list()]
NAcat = NA.select_dtypes(include='object')
NAnum = NA.select_dtypes(exclude='object')
print('We have :',NAcat.shape[1],'categorical features with missing values')
print('We have :',NAnum.shape[1],'numerical features with missing values')

# %%
#MasVnrArea: Masonry veneer area in square feet, the missing data means no veneer so we fill with 0
data['MasVnrArea']=data.MasVnrArea.fillna(0)
#LotFrontage has 16% missing values. We fill with the median
data['LotFrontage']=data.LotFrontage.fillna(data.LotFrontage.median())
#GarageYrBlt:  车库建成的年份
data['GarageYrBlt']=data["GarageYrBlt"].fillna(1980)
#对于剩下的列: Bathroom, half bathroom, basement related columns and garage related columns:
#我们会把它们的缺失值填充为0，即boolean值

# 下面是一些不好人工判断填充值的列，我们把它们填充为前一个数据的对应值。
fill_cols = ['Electrical', 'SaleType', 'KitchenQual', 'Exterior1st',
             'Exterior2nd', 'Functional', 'Utilities', 'MSZoning']
for col in data[fill_cols]:
    data[col] = data[col].fillna(method='ffill')
# method = 'ffill’意味着用前一个数据的对应值来填充

# 对于其他列，我们直接用0填充或者用None填充
#Categorical missing values
NAcols=data.columns
for col in NAcols:
    if data[col].dtype == "object":
        data[col] = data[col].fillna("None")

#Numerical missing values
for col in NAcols:
    if data[col].dtype != "object":
        data[col]= data[col].fillna(0)



# %%特征工程=================================================
data['TotalArea'] = data['TotalBsmtSF'] + data['1stFlrSF'] + data['2ndFlrSF'] + data['GrLivArea'] +data['GarageArea']
data['Bathrooms'] = data['FullBath'] + data['HalfBath']*0.5 
data['Year average']= (data['YearRemodAdd']+data['YearBuilt'])/2
data['MSSubClass'] = data['MSSubClass'].apply(str)
data['YrSold'] = data['YrSold'].astype(str)
# 独热编码
new_data = pd.get_dummies(data)
print("the shape of the original dataset",data.shape)
print("the shape of the encoded dataset",new_data.shape)
print("We have ",new_data.shape[1]- data.shape[1], 'new encoded features')
# 重新划分训练集测试集
train = new_data[:len(data_train)]
test = new_data[len(data_train):]
# 去掉一些离群点
train=train[(train['GrLivArea'] < 4600) & (train['MasVnrArea'] < 1500)]
target = data_train[['SalePrice']]
pos = [1298,523,279]
target.drop(target.index[pos],inplace=True)
print( 'Train: ',train.shape[0], 'rows')
print('Target:', target.shape[0],'rows')


# %%
print("Skewness before log transform: ", target['SalePrice'].skew())
print("Kurtosis before log transform: ",target['SalePrice'].kurt())
#log transform the target:
target["SalePrice"] = np.log1p(target["SalePrice"])
# log1p = log（x+1）
print("Skewness after log transform: ", target['SalePrice'].skew())
print("Kurtosis after log transform: ",target['SalePrice'].kurt())


# %% 模型训练=============================================
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import RobustScaler
# %%预处理
x_train,x_test,y_train,y_test = train_test_split(train,target,test_size=0.3,
    random_state = 0)
scalar = RobustScaler()
x_train = scalar.fit_transform(x_train)
x_test = scalar.fit_transform(x_test)
testX = scalar.transform(test)
y_train = np.array(y_train)
y_test = np.array(y_test)
#%% 线性回归模型
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import cross_val_score
lreg = LinearRegression()
# lreg.fit(x_train,y_train)
mses = cross_val_score(lreg,x_train,y_train,scoring="neg_mean_squared_error",cv=5)
meanMSE = np.mean(mses)
print(meanMSE)
print('RMSE='+str(np.sqrt(-meanMSE)))

# %%
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import mean_squared_error
from sklearn.linear_model import Ridge
import math
ridge = Ridge()
# parameters= {'alpha':[0.0001,0.0009,0.001,0.002,0.003,0.01,0.1,1,10,100]}
parameters = {'alpha':[x for x in range(1,101)]}
ridge_reg = GridSearchCV(ridge,param_grid=parameters,scoring='neg_mean_squared_error',cv=15)
ridge_reg.fit(x_train,y_train)
print("The best value of Alpha is: ",ridge_reg.best_params_)
print("The best score achieved is: ",math.sqrt(-ridge_reg.best_score_))
ridge_pred=math.sqrt(-ridge_reg.best_score_)
#%%
ridge_mod=Ridge(alpha=20)
ridge_mod.fit(x_train,y_train)
y_pred_train=ridge_mod.predict(x_train)
y_pred_test=ridge_mod.predict(x_test)
print('Root Mean Square Error train = ' + str(math.sqrt(mean_squared_error(y_train, y_pred_train))))
print('Root Mean Square Error test = ' + str(math.sqrt(mean_squared_error(y_test, y_pred_test))))   
# %%
from sklearn.linear_model import Lasso
parameters= {'alpha':[0.0001,0.0009,0.001,0.002,0.003,0.01,0.1,1,10,100]}
lasso=Lasso()
lasso_reg=GridSearchCV(lasso, param_grid=parameters, scoring='neg_mean_squared_error', cv=15)
lasso_reg.fit(x_train,y_train)
print('The best value of Alpha is: ',lasso_reg.best_params_)
#%%
lasso_mod=Lasso(alpha=0.0009)
lasso_mod.fit(x_train,y_train)
y_lasso_train=lasso_mod.predict(x_train)
y_lasso_test=lasso_mod.predict(x_test)
#%%
print('Root Mean Square Error train = ' + str(math.sqrt(mean_squared_error(y_train, y_lasso_train))))
print('Root Mean Square Error test = ' + str(math.sqrt(mean_squared_error(y_test, y_lasso_test))))

# %%
coefs = pd.Series(lasso_mod.coef_, index = train.columns)

imp_coefs = pd.concat([coefs.sort_values().head(10),
                     coefs.sort_values().tail(10)])
imp_coefs.plot(kind = "barh", color='yellowgreen')
plt.xlabel("Lasso coefficient", weight='bold')
plt.title("Feature importance in the Lasso Model", weight='bold')
plt.show()
#%%
print("Lasso kept ",sum(coefs != 0), "important features and dropped the other ", sum(coefs == 0)," features")
# %%
from sklearn.linear_model import ElasticNetCV
# 这里alphas是前面网格搜索的最佳结果
alphas = [0.000542555]
l1ratio = [0.1, 0.3,0.5, 0.9, 0.95, 0.99, 1]
elastic_cv = ElasticNetCV(cv=5, max_iter=1e7, alphas=alphas,  l1_ratio=l1ratio)
elasticmod = elastic_cv.fit(x_train, y_train.ravel())
ela_pred=elasticmod.predict(x_test)
ela_train = elasticmod.predict(x_train)
#%%
print('Root Mean Square Error train = ' + str(math.sqrt(mean_squared_error(y_train, ela_train))))
print('Root Mean Square Error test = ' + str(math.sqrt(mean_squared_error(y_test, ela_pred))))
# print(elastic_cv.alpha_)
# print(elastic_cv.l1_ratio_)

# %%
from xgboost.sklearn import XGBRegressor
xgb= XGBRegressor(base_score=0.5, booster='gbtree', colsample_bylevel=1,
             colsample_bynode=1, colsample_bytree=0.5, gamma=0,
             importance_type='gain', learning_rate=0.01, max_delta_step=0,
             max_depth=3, min_child_weight=0, missing=None, n_estimators=4000,
             n_jobs=1, nthread=None, objective='reg:squarederror', random_state=0,
             reg_alpha=0.0001, reg_lambda=0.01, scale_pos_weight=1, seed=None,
             silent=None, subsample=1, verbosity=0)
xgmod=xgb.fit(x_train,y_train)
xg_pred=xgmod.predict(x_test)
xg_train = xgmod.predict(x_train)
print('Root Mean Square Error train = ' + str(math.sqrt(mean_squared_error(y_train, xg_train))))
print('Root Mean Square Error test = ' + str(math.sqrt(mean_squared_error(y_test, xg_pred))))


# %%
from sklearn.ensemble import VotingRegressor

vote_mod = VotingRegressor([('Ridge', ridge_mod), ('Lasso', lasso_mod), ('Elastic', elastic_cv), 
                            ('XGBRegressor', xgb)])
vote= vote_mod.fit(x_train, y_train.ravel())
vote_pred=vote.predict(x_test)
vote_train = vote.predict(x_train)
print('Root Mean Square Error train = ' + str(math.sqrt(mean_squared_error(y_train, vote_train))))
print('Root Mean Square Error test = ' + str(math.sqrt(mean_squared_error(y_test, vote_pred))))
#%%
vote_test = vote_mod.predict(testX)
final=np.expm1(vote_test)
final_submission = pd.DataFrame({
        "Id": data_test["Id"],
        "SalePrice": final
    })
final_submission.to_csv("final_submission2.csv", index=False)
final_submission.head()
