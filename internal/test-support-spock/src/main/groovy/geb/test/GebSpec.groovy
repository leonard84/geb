/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.test

import geb.Browser
import geb.Configuration
import geb.ConfigurationLoader
import geb.spock.SpockGebTestManagerBuilder
import geb.transform.DynamicallyDispatchesToBrowser
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import java.util.function.Supplier

@DynamicallyDispatchesToBrowser
class GebSpec extends geb.spock.GebReportingSpec {

    private static final GebTestManager TEST_MANAGER = managerBuilder()
            .withBrowserCreator(configToBrowserSupplier { new ConfigurationLoader().conf })
            .build()

    @Override
    @Delegate(includes = ["getBrowser", "report"])
    GebTestManager getTestManager() {
        TEST_MANAGER
    }

    protected static Supplier<Browser> configToBrowserSupplier(Supplier<Configuration> configSupplier) {
        { ->
            def browser = new Browser(configSupplier.get())
            if (browser.driver instanceof HtmlUnitDriver) {
                browser.driver.javascriptEnabled = true
            }
            browser
        }
    }

    protected static GebTestManagerBuilder managerBuilder() {
        new SpockGebTestManagerBuilder()
                .withReportingEnabled(true)
    }
}