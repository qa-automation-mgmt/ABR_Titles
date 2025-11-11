package com.seg.titles.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private WebDriver driver;

    // Private table element
    @FindBy(css = "#root > div:nth-child(2) > div > div > main > div.flex.flex-col.overflow-hidden.w-full.static.false > div:nth-child(2) > div > div.w-full > div.flex.p-4.w-full > div.font-poppins.w-\\[80\\%\\].mx-4 > div > div > div.MuiTableContainer-root.css-mvetrv")
    private WebElement table;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Getter method for table element
    public WebElement getTable() {
        return table;
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
