package cn.darkfog.dialog_manager

import android.content.pm.PackageInfo
import cn.darkfog.foundation.arch.AppContextLinker

object AppsRepository {

    fun loadAllApps(): List<PackageInfo> {
        return AppContextLinker.context.packageManager.getInstalledPackages(0)
    }

}