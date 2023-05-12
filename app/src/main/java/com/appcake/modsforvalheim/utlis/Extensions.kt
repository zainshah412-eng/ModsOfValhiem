import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.text.*
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.PermissionRequest
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.utlis.LoggerUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import org.jsoup.Jsoup
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 * @author Antonio Leiva
 * @see <a href="https://antonioleiva.com/kotlin-ongloballayoutlistener/>Kotlin recipes: OnGlobalLayoutListener</a>
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * Extension method to simplify the code needed to apply spans on a specific sub string.
 */
inline fun SpannableStringBuilder.withSpan(
    vararg spans: Any,
    action: SpannableStringBuilder.() -> Unit,
):
        SpannableStringBuilder {
    val from = length
    action()

    for (span in spans) {
        setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to provide show keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

/**
 * Extension method to provide hide keyboard for [View].
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Extension method to int time to 2 digit String
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()

/**
 * Extension method to provide quicker access to the [LayoutInflater] from [Context].
 */
fun Context.getLayoutInflater() =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Extension method to provide quicker access to the [LayoutInflater] from a [View].
 */
fun View.getLayoutInflater() = context.getLayoutInflater()


/**
 * Extension method to get ClickableSpan.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString().trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.validate(message: String, validator: (String) -> Boolean): Boolean {
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.getString())) null else message

    return validator(this.getString())
}

fun EditText.getString(): String {
    return this.text.toString()
}

/**
 * @return Trimmed String value of the EditTextView
 * */
fun EditText.getStringTrim(): String {
    return this.getString().trim()
}

fun String.isValidField(): Boolean = this.isNotEmpty()

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun EditText.updateLabel(editText: EditText, myCalendar: Calendar) {
    val myFormat = "yyyy/MM/dd" //In which you need put here
    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
    editText.setText(sdf.format(myCalendar.time))
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextView.spannableString(view: TextView, str: String, start: Int, end: Int) {
    val spannable = SpannableString(str)
    spannable.setSpan(
        ForegroundColorSpan(view.context.resources.getColor(R.color.purple_200, null)),
        start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(UnderlineSpan(), start, end, 0)
    view.text = spannable
}

fun TextView.spannableStringBold(str: String, start: Int, end: Int) {
    val spannable = SpannableString(str)
    val boldSpan = StyleSpan(Typeface.BOLD)
    spannable.setSpan(boldSpan, start, end, 0)
    this.text = spannable
}

fun ImageView.loadImage(url: String, placeHolder: Int) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .placeholder(placeHolder)
        .error(placeHolder)
        .into(this)
}

fun ImageView.loadDrawable(drawable: Int) {
    ResourcesCompat.getDrawable(
        resources,
        drawable,
        null
    )
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.customBackgroundColor(color: Int) {
    resources.getColor(
        color,
        null
    )
}

fun getListRemainingSize(startSize: Int, endSize: Int): Int {
    if (endSize - startSize > 9) {
        return startSize + 9
    } else {
        return startSize + (endSize - startSize)
    }
}


fun Context.appInstalledOrNot(uri: String): Boolean {
    val pm: PackageManager = packageManager
    try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun showHideProgressDialog(isShown: Boolean, kProgressHUD: KProgressHUD) {
    LoggerUtils.info("Loader", "Loading called $isShown")
    if (isShown) kProgressHUD.show()
    else kProgressHUD.dismiss()
}

fun Context.initializeLoader(): KProgressHUD {
    val kProgressHUD = KProgressHUD.create(this)
    kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
    kProgressHUD.setLabel("Please wait")
    kProgressHUD.setCancellable(false)
    kProgressHUD.setAnimationSpeed(2)
    kProgressHUD.setDimAmount(0.5f)
    return kProgressHUD
}

fun Context.checkVersion(version: String): Boolean {
    val pinfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
    val versionName: String = pinfo.versionName.replace(".", "")
    val versionFromWeb = version.replace(".", "")
    LoggerUtils.info("Ext", "version from web: $versionFromWeb and internal: $versionName")
    if (versionName >= versionFromWeb) {
        return true
    }
    return false
}


fun Activity.calculateTheScreenMetrics(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

fun Context.dpToPixels(dp: Int): Int {
    val displayMetrics = this.resources.displayMetrics;
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt();
}


fun isNumber(s: String?): Boolean {
    var flag = false
    if (s != null) {
        for (c in s.toCharArray()) {
            if (c.isDigit()) {
                flag = true
                break
            }
        }
    }
    return flag
}

fun Context.showSnackBar(text: String, view: View) {
    val parentLayout = view.findViewById<View>(android.R.id.content)
    val snack: Snackbar = Snackbar.make(parentLayout, text, Snackbar.LENGTH_LONG)
    snack.setTextColor(resources.getColor(R.color.white))
    snack.setBackgroundTint(resources.getColor(R.color.black))
    val view = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params
    snack.show()

}

fun Context.showToastSkins(msgSkins: String, durationSkins: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msgSkins, durationSkins).show()
}


fun Context.checkPermissionsDexterSe(
    vararg permissionsSE: String,
    permGrantedSE: (Boolean) -> Unit
) {
    Dexter.withContext(this).withPermissions(
        *permissionsSE
    ).withListener(object : MultiplePermissionsListener {

        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (!report.areAllPermissionsGranted()) {
                if (report.isAnyPermissionPermanentlyDenied) {
                    permGrantedSE(false)
                }
            } else {
                permGrantedSE(true)
            }
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: List<com.karumi.dexter.listener.PermissionRequest?>?, token: PermissionToken?
        ) {
            token?.continuePermissionRequest()
        }
    }).check()
}

@RequiresApi(Build.VERSION_CODES.M)
fun verifyPermissions(activity: Activity): Boolean? {
    var bSomethingFound = false
    val permissons = java.util.ArrayList<String>()

    //Storage
    if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        permissons.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        bSomethingFound = true
    }

    if (bSomethingFound) {
        //Ask permissions
        val items = permissons.toTypedArray()
        activity.requestPermissions(items, 1)
        return false
    }
    return true
}



