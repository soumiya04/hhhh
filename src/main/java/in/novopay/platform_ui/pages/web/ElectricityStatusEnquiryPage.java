package in.novopay.platform_ui.pages.web;

import java.awt.AWTException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.novopay.platform_ui.utils.BasePage;
import in.novopay.platform_ui.utils.CommonUtils;
import in.novopay.platform_ui.utils.DBUtils;
import in.novopay.platform_ui.utils.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class ElectricityStatusEnquiryPage extends BasePage {
	DBUtils dbUtils = new DBUtils();
	CommonUtils commonUtils = new CommonUtils(wdriver);
	DecimalFormat df = new DecimalFormat("#.00");

	@FindBy(xpath = "//h1[contains(text(),'Status Enquiry')]")
	WebElement pageTitle;

	@FindBy(xpath = "//select2[contains(@id,'status-enquiry-product')]/parent::div")
	WebElement product;

	@FindBy(xpath = "//li[contains(text(),'Bill Payment')]")
	WebElement billPaymentProduct;

	@FindBy(id = "status-enquiry-txn-id")
	WebElement txnId;

	@FindBy(id = "status-enquiry-customer-mobile-number")
	WebElement seCustMobNum;

	@FindBy(id = "status-enquiry-txn-id")
	WebElement enterSetxnId;

	@FindBy(xpath = "//div[@status-enquiry='']//button")
	WebElement statusEnquirySubmitButton;

	@FindBy(id = "searchByMobileNumber")
	WebElement pageCustMobNum;

	@FindBy(id = "searchByTxnID")
	WebElement pageTxnId;

	@FindBy(xpath = "//*[@id='reports-list-status-enquiry']//button")
	WebElement pageSubmitButton;

	@FindBy(xpath = "//span[contains(text(),'Bill Payment - Status Enquiry')]")
	WebElement reportPage;

	@FindBy(xpath = "//h4[contains(text(),'!')]")
	WebElement seTxnTitle;

	@FindBy(xpath = "//h4[contains(text(),'!')]/parent::div/following-sibling::div//span")
	WebElement seTxnSuccessMessage;

	@FindBy(xpath = "//h4[contains(text(),'!')]/parent::div/following-sibling::div//span/following-sibling::span")
	WebElement seTxnTimeoutMessage;

	@FindBy(xpath = "//h4[contains(text(),'!')]/parent::div/following-sibling::div/div")
	WebElement seTxnRefundedMessage;

	@FindBy(xpath = "//strong[contains(text(),'Bill Amount:')]/parent::div/following-sibling::div")
	WebElement seTxnScreenTxnAmount;

	@FindBy(xpath = "//strong[contains(text(),'Charges:')]/parent::div/following-sibling::div")
	WebElement seTxnScreenCharges;

	@FindBy(xpath = "//strong[contains(text(),'Refunded Amount:')]/parent::div/following-sibling::div")
	WebElement seTxnScreenRefundedAmount;

	@FindBy(xpath = "//strong[contains(text(),'Failed Amount:')]/parent::div/following-sibling::div")
	WebElement seTxnScreenFailedAmount;

	@FindBy(xpath = "//div/button[contains(text(),'Done')]")
	WebElement seDoneBtn;

	@FindBy(xpath = "//div/button[contains(text(),'Retry')]")
	WebElement seRetryBtn;

	@FindBy(xpath = "//div/button[contains(text(),'Exit')]")
	WebElement seExitBtn;

	@FindBy(xpath = "//h4[contains(text(),'Failed!')]/parent::div/following-sibling::div//span")
	WebElement failSeTxnMsg;

	@FindBy(xpath = "//div/span/button[contains(text(),'Resend OTP')]")
	WebElement seResendOTPBtn;

	@FindBy(xpath = "//div/button[contains(text(),'Initiate Refund')]")
	WebElement failSeInitiateRefundBtn;

	@FindBy(xpath = "//div/button[contains(text(),'Close')]")
	WebElement closeRefundBtn;

	@FindBy(xpath = "//h4[contains(text(),'Confirm Refund')]")
	WebElement confirmRefund;

	@FindBy(xpath = "//div/button[contains(text(),'Ok')]")
	WebElement confirmRefundOkBtn;

	@FindBy(xpath = "//h5[contains(text(),'Enter Customer OTP')]")
	WebElement custOTPScreen;

	@FindBy(id = "money-transfer-otp-number")
	WebElement custOTP;

	@FindBy(xpath = "//*[@id='money-transfer-otp-number']/parent::div//li")
	WebElement custOTPError;

	@FindBy(xpath = "//div/button[contains(text(),'Confirm')]")
	WebElement otpConfirmBtn;

	@FindBy(xpath = "//div/button[contains(text(),'Cancel')]")
	WebElement otpCancelBtn;

	@FindBy(xpath = "//div//button[contains(text(),'Resend')]")
	WebElement otpResendBtn;

	@FindBy(xpath = "//div[contains(@class,'toast-message')]")
	WebElement toasterMsg;

	@FindBy(xpath = "//*[@class='slimScrollBar']")
	WebElement scrollBar;

	@FindBy(xpath = "//span[contains(text(),'Reports')]")
	WebElement reports;

	@FindBy(xpath = "//label[contains(text(),'Select Report to View')]")
	WebElement reportsPage;

	@FindBy(xpath = "//*[@class='fa fa-bars fa-lg text-white']")
	WebElement menu;

	@FindBy(xpath = "//*[@id='reportsDropDown']//span[contains(@class,'select2-selection')]")
	WebElement reportsDropdown;

	@FindBy(xpath = "//li[1][contains(@class,'notifications')]//strong")
	WebElement fcmHeading;

	@FindBy(xpath = "//li[1][contains(@class,'notifications')]/span[2]")
	WebElement fcmContent;

	String txnID = txnDetailsFromIni("GetTxnRefNo", "");

	public ElectricityStatusEnquiryPage(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
	}

	// Perform action on page based on navigation key
	public void electricityStatusEnquiry(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {
		try {
			if (usrData.get("ASSERTION").contains("FCM")) {
				clickElement(menu);
				clickElement(menu);
				assertionOnRefundFCM(usrData);
			} else {
				if (usrData.get("TYPE").equalsIgnoreCase("Section")) {
					statusEnquirySection(usrData);
					clickElement(menu);
					clickElement(menu);
					Thread.sleep(2000);
				} else if (usrData.get("TYPE").equalsIgnoreCase("Page")) {
					clickElement(menu);
					scrollElementDown(scrollBar, reports);
					Log.info("Reports option clicked");
					waitUntilElementIsClickableAndClickTheElement(reportsPage);

					if (usrData.get("TXNDETAILS").equalsIgnoreCase("MobNum")) {
						waitUntilElementIsClickableAndClickTheElement(pageCustMobNum);
						pageCustMobNum.sendKeys(getCustomerDetailsFromIni("ExistingNum"));
						Log.info("Customer mobile number entered");
					} else if (usrData.get("TXNDETAILS").equalsIgnoreCase("TxnID")) {
						waitUntilElementIsClickableAndClickTheElement(pageTxnId);
						pageTxnId.sendKeys(txnID);
						Log.info("Txn ID entered");
					} else {
						waitUntilElementIsClickableAndClickTheElement(pageTxnId);
						pageTxnId.sendKeys(usrData.get("TXNDETAILS"));
					}

					waitUntilElementIsClickableAndClickTheElement(pageSubmitButton);
					Log.info("Submit button clicked");
					Thread.sleep(3000);
					commonUtils.waitForSpinner();
				}
				if (usrData.get("TXNDETAILS").equalsIgnoreCase("11112222")) {
					waitUntilElementIsVisible(toasterMsg);
					Assert.assertEquals("No transaction history for this transaction id", toasterMsg.getText());
				} else {
					reportsData(usrData);
					commonUtils.selectTxn();
					Log.info("Status enquiry of " + usrData.get("STATUS") + " Transaction");
					Thread.sleep(1000);
					waitUntilElementIsVisible(seTxnTitle);
					assertionOnTxnScreen(usrData);
					if (usrData.get("STATUS").equalsIgnoreCase("Success")) {
						seDoneBtn.click();
					} else if (usrData.get("STATUS").equalsIgnoreCase("Auto-Refunded")
							|| usrData.get("STATUS").equalsIgnoreCase("Late-Refunded")) {
						seDoneBtn.click();
					} else if (usrData.get("STATUS").equalsIgnoreCase("Timeout")) {
						seDoneBtn.click();
					} else if (usrData.get("STATUS").equalsIgnoreCase("To_Be_Refunded")) {
						closeRefundBtn.click();
					} else if (usrData.get("STATUS").equalsIgnoreCase("Refund")) {
						Thread.sleep(1000);
						waitUntilElementIsClickableAndClickTheElement(failSeInitiateRefundBtn);
						Thread.sleep(1000);
						waitUntilElementIsClickableAndClickTheElement(confirmRefund);
						Thread.sleep(1000);
						waitUntilElementIsVisible(custOTPScreen);
						custOTP.click();
						if (usrData.get("OTP").equalsIgnoreCase("Valid")) {
							custOTP.sendKeys(getAuthfromIni(otpFromIni()));
							Log.info("Refund OTP entered");
							waitUntilElementIsClickableAndClickTheElement(otpConfirmBtn);
							commonUtils.waitForSpinner();
							waitUntilElementIsVisible(seTxnTitle);
							waitUntilElementIsClickableAndClickTheElement(seDoneBtn);
							commonUtils.waitForSpinner();
							waitUntilElementIsVisible(pageTxnId);
							Log.info("Refund successful");
						} else if (usrData.get("OTP").equalsIgnoreCase("Invalid")
								|| usrData.get("OTP").equalsIgnoreCase("Retry")) {
							custOTP.sendKeys("111111");
							Log.info("Refund OTP entered");
							waitUntilElementIsClickableAndClickTheElement(otpConfirmBtn);
							commonUtils.waitForSpinner();
							waitUntilElementIsVisible(seTxnTitle);
							Assert.assertEquals("Invalid Verification Code", failSeTxnMsg.getText());
							Log.info(failSeTxnMsg.getText());
							if (usrData.get("OTP").equalsIgnoreCase("Retry")) {
								seRetryBtn.click();
								commonUtils.waitForSpinner();
								waitUntilElementIsVisible(custOTPScreen);
								custOTP.click();
								custOTP.sendKeys(getAuthfromIni(otpFromIni()));
								Log.info("Refund OTP entered");
								waitUntilElementIsClickableAndClickTheElement(otpConfirmBtn);
								commonUtils.waitForSpinner();
								waitUntilElementIsVisible(seTxnTitle);
								assertionOnTxnScreen(usrData);
								seDoneBtn.click();
								commonUtils.waitForSpinner();
								waitUntilElementIsVisible(pageTxnId);
								Log.info("Refund successful");
							} else {
								seExitBtn.click();
							}
						} else if (usrData.get("OTP").equalsIgnoreCase("Cancel")) {
							otpCancelBtn.click();
						} else if (usrData.get("OTP").equalsIgnoreCase("Resend")) {
							waitUntilElementIsClickableAndClickTheElement(otpResendBtn);
							waitUntilElementIsVisible(custOTPScreen);
							custOTP.click();
							custOTP.sendKeys(getAuthfromIni(otpFromIni()));
							Log.info("Refund OTP entered");
							waitUntilElementIsClickableAndClickTheElement(otpConfirmBtn);
							commonUtils.waitForSpinner();
							waitUntilElementIsVisible(seTxnTitle);
							seDoneBtn.click();
							commonUtils.waitForSpinner();
							waitUntilElementIsVisible(pageTxnId);
							Log.info("Refund successful");
						}
					}
				}
			}
		} catch (Exception e) {
			wdriver.navigate().refresh();
			e.printStackTrace();
			Log.info("Test Case Failed");
			Assert.fail();
		}
	}

	public void statusEnquirySection(Map<String, String> usrData)
			throws InterruptedException, AWTException, IOException, ClassNotFoundException {
		waitUntilElementIsClickableAndClickTheElement(product);
		Log.info("Status Enquiry drop down clicked");

		waitUntilElementIsClickableAndClickTheElement(billPaymentProduct);
		Log.info("Bill Payment selected");

		if (usrData.get("TXNDETAILS").equalsIgnoreCase("MobNum")) {
			waitUntilElementIsClickableAndClickTheElement(seCustMobNum);
			seCustMobNum.sendKeys(getCustomerDetailsFromIni("ExistingNum"));
			Log.info("Customer mobile number entered");
		} else if (usrData.get("TXNDETAILS").equalsIgnoreCase("TxnID")) {
			waitUntilElementIsClickableAndClickTheElement(enterSetxnId);
			enterSetxnId.sendKeys(txnID);
			Log.info("Txn ID entered");
		} else {
			waitUntilElementIsClickableAndClickTheElement(enterSetxnId);
			enterSetxnId.sendKeys(usrData.get("TXNDETAILS"));
		}

		waitUntilElementIsClickableAndClickTheElement(statusEnquirySubmitButton);
		Log.info("Submit button clicked");
		commonUtils.waitForSpinner();
		waitUntilElementIsVisible(reportPage);
		Log.info("Report page displayed");
	}

	public void reportsData(Map<String, String> usrData) throws ClassNotFoundException {
		String[][] dataFromUI = new String[1][5];
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 5; j++) {
				String dataXpath = "//tbody/tr[" + (i + 1) + "]/td[" + (j + 1) + "]";
				waitUntilElementIsVisible(wdriver.findElement(By.xpath(dataXpath)));
				WebElement data = wdriver.findElement(By.xpath(dataXpath));
				dataFromUI[i][j] = data.getText();
			}
		}
		String statusXpath = "//tbody/tr[1]/td[6]";
		WebElement statusData = wdriver.findElement(By.xpath(statusXpath));
		if (usrData.get("STATUS").equalsIgnoreCase("Success")) {
			Assert.assertEquals(statusData.getText(), "SUCCESS");
		} else if (usrData.get("STATUS").equalsIgnoreCase("Pending")) {
			Assert.assertEquals(statusData.getText(), "PENDING");
		} else if (usrData.get("STATUS").equalsIgnoreCase("Failed")) {
			Assert.assertEquals(statusData.getText(), "FAILED");
		}
		Log.info(statusData.getText());

		List<String> listFromUI = new ArrayList<String>();
		for (String[] array : dataFromUI) {
			listFromUI.addAll(Arrays.asList(array));
		}

		List<String[]> list = new ArrayList<String[]>();
		List<String[]> mtStatusEnquiry = dbUtils.mtStatusEnquiry(txnID);
		list = mtStatusEnquiry;

		List<String> listFromDB = new ArrayList<String>();
		for (String[] data : list) {
			for (String s : data) {
				listFromDB.add(s);
			}
		}

		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<String> iUI = listFromUI.iterator();
		Iterator<String> iDB = listFromDB.iterator();
		while (iUI.hasNext() && iDB.hasNext()) {
			map.put(iUI.next(), iDB.next());
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Assert.assertEquals(entry.getKey(), entry.getValue());
		}
		for (String u : listFromUI) {
			System.out.print(u + " | ");
		}
		System.out.println();
	}

	// Verify details on txn screen
	public void assertionOnTxnScreen(Map<String, String> usrData)
			throws ClassNotFoundException, ParseException, InterruptedException {
		if (usrData.get("STATUS").equalsIgnoreCase("Success")) {
			Assert.assertEquals(replaceSymbols(seTxnScreenTxnAmount.getText()),
					txnDetailsFromIni("GetTxfAmount", "") + ".00");
			Log.info("Transferred Amount: " + replaceSymbols(seTxnScreenTxnAmount.getText()));
		} else if (usrData.get("STATUS").equalsIgnoreCase("Failed")) {
			Assert.assertEquals(replaceSymbols(seTxnScreenRefundedAmount.getText()),
					txnDetailsFromIni("GetFailAmount", "") + ".00");
			Log.info("Refunded Amount: " + replaceSymbols(seTxnScreenRefundedAmount.getText()));
		}
		Assert.assertEquals(replaceSymbols(seTxnScreenCharges.getText()), txnDetailsFromIni("GetCharges", ""));
		Log.info("Charges: " + replaceSymbols(seTxnScreenCharges.getText()));
	}

	// FCM assertion
	public void assertionOnRefundFCM(Map<String, String> usrData) throws ClassNotFoundException {
		double amount = Double.parseDouble(txnDetailsFromIni("GetTxfAmount", ""));
		double charges = Double.parseDouble(txnDetailsFromIni("GetCharges", ""));
		double totalAmount = amount + charges;

		String successFCMHeading = "Refund : SUCCESS";
		String failFCMHeading = "Refund : FAIL";

		String successFCMContent = "Customer Mobile: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Money Transfer with cash payment: Rs." + df.format(amount) + " and Charges:Rs." + df.format(charges)
				+ ", Total: Rs." + df.format(totalAmount)
				+ " has been refunded to Agent Wallet.Transaction Reference No. " + dbUtils.paymentRefCode(partner());
		String failFCMContent = "Customer Mobile: " + getCustomerDetailsFromIni("ExistingNum")
				+ " Invalid Verification Code.";

		switch (usrData.get("ASSERTION")) {
		case "Success FCM":
			fcmMethod(successFCMHeading, successFCMContent);
			break;
		case "Failed FCM":
			fcmMethod(failFCMHeading, failFCMContent);
			break;
		}
	}

	public void fcmMethod(String heading, String content) {
		Assert.assertEquals(fcmHeading.getText(), heading);
		Assert.assertEquals(fcmContent.getText(), content);
		Log.info(fcmHeading.getText());
		Log.info(fcmContent.getText());
	}

	// Get Partner name
	public String partner() {
		return "RBL";
	}

	// Get otp from Ini file
	public String otpFromIni() {
		return partner().toUpperCase() + "OTP";
	}

}