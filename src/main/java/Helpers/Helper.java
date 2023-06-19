package Helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class Helper {

    private WebDriver _driver;
    private WebElement _current;

	 public static int TimeoutElement = 2;
	 
    /**  ------------ The new age helper ---------------- 
     * 
     *  (new Helper(driver))
     *      .current( myElement).setValue( value )
     *      .current( form ).click()
     *      .waitUpdate()
     *      ;
    */
	public Helper(WebDriver driver) {
        _driver = driver;
        _driver.manage().timeouts().implicitlyWait(TimeoutElement, TimeUnit.SECONDS);
	}
    public Helper(WebDriver driver, int timeout) {
        _driver = driver;
		  TimeoutElement = timeout;
        _driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public Helper current(WebElement element) {
        _current = element;
        return this;
    }
    public Helper current(By by) {
		 _driver.manage().timeouts().implicitlyWait(TimeoutElement, TimeUnit.SECONDS);
       _current = _driver.findElement(by);
        return this;
    }

    public WebElement current(){
        return _current;
    }
    public boolean tryFindBy (By by){
        try {
            _driver.manage().timeouts().implicitlyWait(TimeoutElement, TimeUnit.SECONDS);
             _driver.findElement(by).isDisplayed();
             _current = _driver.findElement(by);
             return true;
        } catch (NoSuchElementException ex) {
            return false;
        }

    }
    public boolean tryFindByTimeWait (By by, long timewait){
        try {
            _driver.manage().timeouts().implicitlyWait(timewait, TimeUnit.SECONDS);
             _driver.findElement(by).isDisplayed();
             _current = _driver.findElement(by);
             return true;
        } catch (NoSuchElementException ex) {
            return false;
        }

    }
    public Helper clear() {
        clear(_current, _driver);
        return this;
    }
    public static void clear(WebElement element, WebDriver driver) {
        Actions navigator = new Actions(driver);
            navigator.click(element) //
                    .sendKeys(Keys.HOME) //
                    /* */.keyDown(Keys.SHIFT) //
                    /*     */.sendKeys(Keys.END) //
                    /* */.keyUp(Keys.SHIFT) //
                    .sendKeys(Keys.BACK_SPACE).build().perform();

    }

    public Helper setValue(String value) {
		 waitUntilConditions(_current, TimeoutElement, _driver);
        //clear();
        _current.sendKeys(value);
        return this;
    }

    public String getText() {
        return _current.getText();
    }

    public Helper moveTo() {
        final Actions navigator = new Actions(_driver);
        navigator.moveToElement(_current).perform();
        return this;
    }

    public Helper doubleClick() {
        Actions navigator = new Actions(_driver);
        navigator.doubleClick(_current).perform();
        return this;
    }

    public Helper click() {
		  waitUntilConditions(_current, TimeoutElement, _driver);
        Actions navigator = new Actions(_driver);
        navigator.click(_current).build().perform();
        return this;
    }

    public String getAttribute(String attr) {
        return _current.getAttribute(attr);
    }


    public static WebElement setValue(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
        element.sendKeys(Keys.TAB);
        return element;
    }

    public static void isHoverElement(WebElement elementhover, WebElement elementclick, WebDriver driver) {

        Actions builder = new Actions(driver);
        builder.moveToElement(elementhover).click(elementclick);
        Action mouseoverAndClick = builder.build();
        mouseoverAndClick.perform();
    }

    public Helper waitSetup(int ms) {
        try {
            Thread.sleep(ms);
        } 
        catch (WebDriverException | InterruptedException exWebDriverException) {
            Assert.fail("Timeout driver. Not enough time to display the item.");
        }
		  return this;
    }

    /**
     *
     * FILE Func
     */
    public void clearDirectory(String path) throws Exception
    {
        for (File file: new File(path).listFiles())  if (file.isFile()) file.delete();
    }
    public String readFile(String path){
        try {
            File file = new File(path);
            String fullPath = file.getAbsolutePath();
            FileReader fileReader = new FileReader(fullPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String inputFile = "";
            String textFieldReadable = bufferedReader.readLine();

            while (textFieldReadable != null){
                inputFile += textFieldReadable;
                textFieldReadable = bufferedReader.readLine();
            }
            return inputFile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Файл не существует!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка в/в!");
        }
       return null;
    }

    private String getAbsPath(String path){
        File file = new File(path);
        String fullPath = file.getAbsolutePath();
        return fullPath;
    }
    public static void exists(String fileName) throws FileNotFoundException {

        File file = new File(fileName);
        if (!file.exists()){
            System.out.println("Файл "+fileName+" не существует!");
            //throw new FileNotFoundException(file.getName());
        }
    }
    public void delete(String nameFile) throws FileNotFoundException {
        String fullPathFile = getAbsPath(nameFile);
        exists(fullPathFile);
        new File(fullPathFile).delete();
    }

    public static String getLogName(String fileName)
	 {
		 return "logs-js/"+ fileName +".log";
	 }
    public static void writeFile(String fileName, String text) {
        //Определяем файл
        File file = new File(fileName);
        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }
            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));

            try {
                //Записываем текст у файл
                out.println(text);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeAllLogInFile(WebDriver webDriver,String nameMethod,String nameClass) throws IOException {

        String filename = nameClass;
        logConsoleJs(webDriver,filename, nameMethod);
    }
	public void logConsoleJs(WebDriver webDriver, String filename, String methodName) throws IOException {

		LogEntries logEntries = webDriver.manage().logs().get("browser");
		String logall;
		if (logEntries == null || logEntries.getAll().size() <= 0) {
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.DD hh:mm:ss");
		writeFile(getLogName(filename), "\n\n--------------------START CONSOLE LOGS - " + methodName + "--------------------\n");
		for (LogEntry entry : logEntries) {
			logall = format.format(new Date(entry.getTimestamp())) + " " + entry.getLevel()
				 + " " + entry.getMessage();
			writeFile(getLogName(filename), logall);
			writeFile(getLogName(filename), "\n");
		}
		writeFile(getLogName(filename), "\n--------------------END CONSOLE LOGS - " + methodName + "--------------------\n");
	}

    public static void log(String msg)
    {
        System.out.println("DEBUG:"+msg);
    }
    public Helper trace(String msg)
    {
        System.out.println("DEBUG:"+msg);
		  return this;
    }
    public static void logError(String msg)
    {
        System.out.println("ERROR:"+msg);
    }

	 public Helper openUrl(String url)
	 {
		 _driver.navigate().to(url);
		 return this;
	 }
	 public void refreshPage(){
         _driver.navigate().refresh();
     }


    /**
     *
     * the method waits for the element to appear
     */
    public static boolean waitUntilConditions(WebElement locator, int time, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            //clickable
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (TimeoutException ex) {
            log("Wait timout. " + time + " secs. Element not clickable: " +locator);
            return false;
        }
    }


}


