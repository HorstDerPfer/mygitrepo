package db.training.easy.web.admin.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserService;
import db.training.security.SecurityService;

public class UserForm extends ValidatorForm {

	private static final long serialVersionUID = -2683444896290608958L;

	private Integer userId;

	private Integer regionalbereichId;

	private String loginName;

	private String name;

	private String firstName;

	private String email;

	private String phonePrefix;

	private String phoneNumber;

	private String phoneInternalPrefix;

	private String phoneInternalNumber;

	private String faxPrefix;

	private String faxNumber;

	private String faxInternalPrefix;

	private String faxInternalNumber;

	private String mobileNumber;

	private boolean changeLogin;

	private boolean resetPassword;

	private boolean generatePassword;

	private boolean disabled;

	private boolean locked;

	private Integer bearbeitungsbereichId;

	private List<Bearbeitungsbereich> bearbeitungsbereichList = new ArrayList<Bearbeitungsbereich>();

	public void reset() {
		userId = null;
		regionalbereichId = null;
		loginName = null;
		name = null;
		firstName = null;
		email = null;
		phonePrefix = null;
		phoneNumber = null;
		phoneInternalPrefix = null;
		phoneInternalNumber = null;
		faxPrefix = null;
		faxNumber = null;
		faxInternalPrefix = null;
		faxInternalNumber = null;
		mobileNumber = null;
		changeLogin = false;
		resetPassword = false;
		generatePassword = false;
		disabled = false;
		locked = false;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		changeLogin = false;
		resetPassword = false;
		generatePassword = false;
		disabled = false;
		locked = false;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);
		UserService service = EasyServiceFactory.getInstance().createUserService();

		if (isChangeLogin() == true && getLoginName() != null && getLoginName().length() > 0) {
			Boolean userError = false;

			// applicationUser *******************************************
			User user = service.findUserByLoginName(getLoginName());
			if (user != null && !user.getId().equals(getUserId()))
				userError = true;

			// securityUser **********************************************
			SecurityService secService = EasyServiceFactory.getInstance().createSecurityService();
			UserDetails secUser = null;
			try {
				secUser = secService.loadUserByUsername(getLoginName());
			} catch (UsernameNotFoundException e) {
			}

			if (secUser != null) {
				if (!getUserId().equals(0)) {
					user = service.findUserById(getUserId());
					if (user.getLoginName() != null)
						if (!user.getLoginName().equals(secUser.getUsername()))
							userError = true;
				} else
					userError = true;
			}

			// Auswertung *************************************************
			if (userError)
				actionErrors.add("error.user.loginName.notunique", new ActionMessage(
				    "error.user.loginName.notunique"));
		}

		if (getEmail().length() > 0) {
			User user = service.findUserByEmail(getEmail());
			if (user != null && !user.getId().equals(getUserId()))
				actionErrors.add("error.user.email.notunique", new ActionMessage(
				    "error.user.email.notunique"));
		}
		return actionErrors;
	}

	public boolean isChangeLogin() {
		return changeLogin;
	}

	public void setChangeLogin(boolean changeLogin) {
		this.changeLogin = changeLogin;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFaxInternalNumber() {
		return faxInternalNumber;
	}

	public void setFaxInternalNumber(String faxInternalNumber) {
		this.faxInternalNumber = faxInternalNumber;
	}

	public String getFaxInternalPrefix() {
		return faxInternalPrefix;
	}

	public void setFaxInternalPrefix(String faxInternalPrefix) {
		this.faxInternalPrefix = faxInternalPrefix;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getFaxPrefix() {
		return faxPrefix;
	}

	public void setFaxPrefix(String faxPrefix) {
		this.faxPrefix = faxPrefix;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public boolean isGeneratePassword() {
		return generatePassword;
	}

	public void setGeneratePassword(boolean generatePassword) {
		this.generatePassword = generatePassword;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneInternalNumber() {
		return phoneInternalNumber;
	}

	public void setPhoneInternalNumber(String phoneInternalNumber) {
		this.phoneInternalNumber = phoneInternalNumber;
	}

	public String getPhoneInternalPrefix() {
		return phoneInternalPrefix;
	}

	public void setPhoneInternalPrefix(String phoneInternalPrefix) {
		this.phoneInternalPrefix = phoneInternalPrefix;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public boolean isResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	public Integer getRegionalbereichId() {
		return regionalbereichId;
	}

	public void setRegionalbereichId(Integer regionalbereichId) {
		this.regionalbereichId = regionalbereichId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Bearbeitungsbereich> getBearbeitungsbereichList() {
		return bearbeitungsbereichList;
	}

	public void setBearbeitungsbereichList(List<Bearbeitungsbereich> bearbeitungsbereichList) {
		this.bearbeitungsbereichList = bearbeitungsbereichList;
	}

	public void setBearbeitungsbereichId(Integer bearbeitungsbereichId) {
		this.bearbeitungsbereichId = bearbeitungsbereichId;
	}

	public Integer getBearbeitungsbereichId() {
		return bearbeitungsbereichId;
	}

}
