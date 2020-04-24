package com.ewingsa.ohyeah.appinjection

import com.ewingsa.ohyeah.MainApplication
import com.ewingsa.ohyeah.appinjection.modules.ApplicationBindingModule
import com.ewingsa.ohyeah.appinjection.scopes.PerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@PerApplication
@Component(modules = [AndroidInjectionModule::class, ApplicationBindingModule::class])
interface ApplicationComponent {

    fun inject(mainApplication: MainApplication)

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun mainApplicationBind(mainApplication: MainApplication): Builder
    }
}
