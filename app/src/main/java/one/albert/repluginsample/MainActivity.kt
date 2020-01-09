package one.albert.repluginsample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.albert.test_maven.SdkManagerInit
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.model.PluginInfo
import com.qihoo360.replugin.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Sample Host
 * Replugin 插件化框架接入
 */
class MainActivity : Activity() {
    //外部插件
//    val pluginFileName = "plugin_apk.apk"
//    val pluginClassName = "one.albert.plugin_apk.MainActivity"
//    val pluginName = "plugin_apk"

    //外部插件
    val pluginFileName = "webview.apk"
    val pluginClassName = "com.qihoo360.replugin.sample.webview.MainActivity"
    val pluginName = "webview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btn(view: View) {
        when (view.id) {
            R.id.btn1 -> {//下载

            }
            R.id.btn2 -> {//安装
                simulateInstallExternalPlugin(pluginFileName, pluginClassName)
            }
            R.id.btn3 -> {//打开
                // 文件是否已经存在？直接删除重来
                if (RePlugin.isPluginInstalled(pluginName)) {
                    RePlugin.startActivity(this, RePlugin.createIntent(pluginName, pluginClassName))
                } else {
                    Toast.makeText(this, "not install plugin", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn4 -> {
                SdkManagerInit.init()
            }
        }
    }


    private fun simulateInstallExternalPlugin(demo3Apk: String, claPath: String) {
        val demo3apkPath = demo3Apk

        // 文件是否已经存在？直接删除重来
        val pluginFilePath = filesDir.absolutePath + File.separator + demo3Apk
        val pluginFile = File(pluginFilePath)
        if (pluginFile.exists()) {
            FileUtils.deleteQuietly(pluginFile)
        }

        // 开始复制
        copyAssetsFileToAppFiles(demo3apkPath, demo3Apk)
        var info: PluginInfo? = null
        if (pluginFile.exists()) {
            info = RePlugin.install(pluginFilePath)
        }

        if (info != null) {
            RePlugin.startActivity(this, RePlugin.createIntent(info.name, claPath))
        } else {
            Toast.makeText(this, "install external plugin failed", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 从assets目录中复制某文件内容
     * @param  assetFileName assets目录下的Apk源文件路径
     * @param  newFileName 复制到/data/data/package_name/files/目录下文件名
     */
    private fun copyAssetsFileToAppFiles(assetFileName: String, newFileName: String) {
        var ism: InputStream? = null
        var fos: FileOutputStream? = null
        val buffsize = 1024

        try {
            ism = this.assets.open(assetFileName)
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE)
            var byteCount = 0
            val buffer = ByteArray(buffsize)
            byteCount = ism!!.read(buffer)
            while (byteCount != -1) {
                fos!!.write(buffer, 0, byteCount)
                byteCount = ism!!.read(buffer)
            }
            fos!!.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                ism!!.close()
                fos!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
