package ar.edu.itba.it.paw.web.user.form;

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.RequiredPasswordTextField;
import ar.edu.itba.it.paw.web.user.create.CreateUser;

public abstract class UserForm extends BasePanel {

	private transient String userName;
	private transient String password;
	@SuppressWarnings("unused")
	private transient String passwordConfirm;
	private transient String email;
	private transient String name;
	private transient String lastName;
	@SuppressWarnings("unused")
	private transient String captchaText;
	
	@SpringBean
	private UserRepository users;
	
	private CaptchaImageResource captchaImageResource;
	
	public UserForm(String id) {
		super(id);
		
		final Form<CreateUser> form = new Form<CreateUser>("createUserForm", new CompoundPropertyModel<CreateUser>(this)) {
			@Override
			protected void onSubmit() {
				UserForm.this.onSubmit(name, lastName, userName, password, email);
			}
		};
		
		@SuppressWarnings("rawtypes")
		final FormComponent[] cmpts = new FormComponent[2];
		
		form.add(new ErrorFeedbackPanel("feedback"));
		
		form.add(new RequiredTextField<String>("userName").add(StringValidator.lengthBetween(4, 30)).add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				if(users.getByUsername(validatable.getValue()) != null) {
					validatable.error(new ValidationError().addMessageKey("userRepeatError"));
				}
			}
		}));
		
		form.add(cmpts[0] = new RequiredPasswordTextField("password").add(StringValidator.lengthBetween(6, 30)));
		form.add(cmpts[1] = new RequiredPasswordTextField("passwordConfirm"));
		form.add(new RequiredTextField<String>("email").add(StringValidator.lengthBetween(4, 60)).add(EmailAddressValidator.getInstance()).add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				if(users.getByEmail(validatable.getValue()) != null) {
					validatable.error(new ValidationError().addMessageKey("emailRepeatError"));
				}
			}
		}));
		form.add(new RequiredTextField<String>("name").add(StringValidator.lengthBetween(4, 30)));
		form.add(new RequiredTextField<String>("lastName").add(StringValidator.lengthBetween(4, 30)));

		form.add(new IFormValidator() {
			@Override
			public void validate(Form<?> form) {
				if(!cmpts[0].getValue().equals(cmpts[1].getValue())) {
					form.error(getString("confirmPasswordError"));
				}
			}
			
			@Override
			public FormComponent<?>[] getDependentFormComponents() {
				return cmpts;
			}
		});
		
		addCaptcha(form, getCurrentUser() == null);
		
		add(form);
	}
	
	private void addCaptcha(final Form<?> form, final boolean visible) {
		if(visible) {
			captchaImageResource = new CaptchaImageResource();
			captchaImageResource.setCacheable(false);
		}
		form.add(new Image("captchaImage", captchaImageResource) {
			@Override
			public boolean isVisible() {
				return visible;
			}
		});
		form.add(new RequiredTextField<String>("captchaText"){

			@Override
			public boolean isVisible() {
				return visible;
			}
			
		}.add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				if(!validatable.getValue().equals(captchaImageResource.getChallengeId())) {
					captchaImageResource.invalidate();
					form.error(getString("invalidCaptcha"));
				}
			}
		}));
	}
	
	protected abstract void onSubmit(String name, String lastName, String userName, String password, String email);

}
