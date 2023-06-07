# Shopping_Cart
This is a project for Java Lesson.
本次提交主要实现了密码找回功能。
1.LoginFrame类：
修改了“忘记密码”按钮的组件名称，由原来的button改成了forgotpwd；增加了处理"忘记密码"按钮事件监听的内部类（113行-123行）。
2.Handler类：
增加了一种Switch的case情况（72-74行），处理获取验证码的情况；增加了一个方法opGetMsg（84-95行），用于随机生成验证码并返回，响应发送验证码的请求。
3.ProtocolPort接口：
新增了一个验证码发送标志OP_SEND_CODE。
4.新写了一个PwdRecoveryFrame类
该类是密码找回界面；生成各组件按钮和进行事件监听。
5.UserDataClient类
新增一个验证码信息属性codeMsg（第19行）；新增请求发送验证码功能的sendCode方法（131-158行）；密码找回验证searchPwd方法（160-189行）。

第三次提交
本次提交解决了“改写购买方法，原本购买后商品只会维持1而不会累加，改为购买后会累加”的问题。
1.Product类：
新增了一个属性，保存商品的购买数量，并增加了getter和setter方法
2.ShoppingCartDialog类：
修改了第102、103、107行，使商品数量进行监听。
新增了一个处理数量输入框（商品的个数）的内部类（131-162行）。
修改了清空购物车按钮的功能，清空购物车时增加了使商品数量置0的操作。