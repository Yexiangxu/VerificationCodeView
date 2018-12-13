仿滴滴输入验证码样式，在网上找了好几个，这个https://github.com/liuguangli/VerificationCodeInput 还不错，于是在这个项目基础上做了修改以满足我们的需求
在上面项目基础上修改点：

1. 设置了editText.setPadding(0, 0, 0, 0); 不然当方框设置比较小的时候文字不居中；
2. 设置字体颜色大小；
3. 显示输入框中光标永远只能设置在数字后面不能设置在前面于是重写了EditText;
4. 输入完发现中间有输入错误，当从中间输入的时候该方框后面全部置空
效果图如下：
![](https://github.com/Yexiangxu/VerificationCodeView/blob/master/didi.gif)


