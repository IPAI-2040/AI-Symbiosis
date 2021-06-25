package cn.darkfog.speech_protocol.function

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import cn.darkfog.foundation.arch.AppContextLinker


// TODO: 2020/8/21  相关的借口全部需要转换成有反馈的
// TODO: 2020/8/21  额外功能支持：打开浏览器，打开设置，打开蓝牙/Wifi，调试模式，设置闹钟，打开便签，
object AppManager {

    private fun openApp(packageInfo: PackageInfo) {
        val context = AppContextLinker.context
        context.startActivity(context.packageManager.getLaunchIntentForPackage(packageInfo.packageName))
    }

    //打开微信扫一扫
    private fun openWechatScaner() {
        // TODO: 2020/8/21 出错处理
        val context = AppContextLinker.context
        val intent: Intent = context.packageManager.getLaunchIntentForPackage("com.tencent.mm")!!
        intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
        context.startActivity(intent)
    }

    //打开支付宝扫一扫
    private fun openAliPayScaner() {
        val uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        AppContextLinker.context.startActivity(intent)
    }

    //打开支付宝付款界面
    private fun openAliPayPayment() {
        val uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000056")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        AppContextLinker.context.startActivity(intent)
    }

    private fun openBaidu() {
        val uri = Uri.parse("https://www.baidu.com")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        AppContextLinker.context.startActivity(intent)
    }

    private fun openBrowser() {

    }

}