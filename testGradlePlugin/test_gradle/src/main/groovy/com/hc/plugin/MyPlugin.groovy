package com.hc.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
//自定义gradle的sdk    https://www.jianshu.com/p/d53399cd507b
public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("========================")
        System.out.println("hello gradle plugin!")
        System.out.println("========================")
    }
}
