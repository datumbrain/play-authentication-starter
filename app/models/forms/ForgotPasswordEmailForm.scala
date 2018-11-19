package models.forms

object ForgotPasswordEmailForm {

  import play.api.data.Form
  import play.api.data.Forms._

  /**
    * A form processing DTO that maps to the form below.
    *
    * Using a class specifically for form binding reduces the chances
    * of a parameter tampering attack and makes code clearer.
    */
  case class ForgotEmail(email: String);
  /**
    * The form definition for the "create a widget" form.
    * It specifies the form fields and their types,
    * as well as how to convert from a Data to form data and vice versa.
    */
  val forgotpasswordemailform = Form(
    mapping(
      "email" -> email,
    )(ForgotEmail.apply)(ForgotEmail.unapply)
  )
}
