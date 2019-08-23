package ahtewlg7.utimer.util;

import androidx.annotation.IntegerRes;

import ahtewlg7.utimer.R;

public class AscII {
    public static char Space(){
        return getAscII(R.integer.Space);//空格
    }
    public static char ExclamationMark(){
        return getAscII(R.integer.ExclamationMark);//叹号!
    }
    public static char NumberSign(){
        return getAscII(R.integer.NumberSign);//井号#
    }
    public static char PercentSign(){
        return getAscII(R.integer.PercentSign);//百分号%
    }
    public static char Ampersand(){
        return getAscII(R.integer.Ampersand);//和号&
    }
    public static char Asterisk(){
        return getAscII(R.integer.Asterisk);//星号*
    }
    public static char PlusSign(){
        return getAscII(R.integer.PlusSign);//加号+
    }
    public static char Comma(){
        return getAscII(R.integer.Comma);//逗号,
    }
    public static char Hyphen(){
        return getAscII(R.integer.Hyphen);//破折号-
    }
    public static char LessThanSign(){
        return getAscII(R.integer.LessThanSign);//小于号<
    }
    public static char GreaterThanSign(){
        return getAscII(R.integer.GreaterThanSign);//大于号>
    }
    public static char QuestionMark(){
        return getAscII(R.integer.QuestionMark);//问号?
    }
    public static char CommercialAtSign(){
        return getAscII(R.integer.CommercialAtSign);//电子邮件符号@
    }
    public static char LeftSquareBracket(){
        return getAscII(R.integer.LeftSquareBracket);//开方括号[
    }
    public static char RightSquareBracket(){
        return getAscII(R.integer.RightSquareBracket);//闭方括号]
    }
    public static char Tilde(){
        return getAscII(R.integer.Tilde);//波浪号~
    }
    public static char YuanSign(){
        return getAscII(R.integer.YuanSign);//人民币¥
    }
    public static char BrokenBar(){
        return getAscII(R.integer.BrokenBar);//竖杠¦
    }
    public static char CopyrightSign(){
        return getAscII(R.integer.CopyrightSign);//版权符©
    }
    public static char LeftDoubleAngleQuotationMark(){
        return getAscII(R.integer.LeftDoubleAngleQuotationMark);//左指双尖引号«
    }
    public static char RegisteredTrademarkSign(){
        return getAscII(R.integer.RegisteredTrademarkSign);//注册商标符®
    }
    public static char DegreeSign(){
        return getAscII(R.integer.DegreeSign);//度符°
    }
    public static char MiddleDot(){
        return getAscII(R.integer.MiddleDot);//黑点符·
    }
    public static char OneQuarter(){
        return getAscII(R.integer.OneQuarter);//1/4
    }
    public static char LatinSmallOWithStroke(){
        return getAscII(R.integer.LatinSmallOWithStroke);//空集符ø
    }
    private static char getAscII(@IntegerRes int ascii){
        return (char)MyRInfo.getIntegerByID(ascii);
    }
}
