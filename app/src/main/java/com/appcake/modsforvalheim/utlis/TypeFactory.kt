package com.appcake.modsforvalheim.utlis

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.appcake.modsforvalheim.R

class TypeFactory(context: Context) {
    var poppinsBold:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_bold)
    var poppinsExtraBold:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_extrabold)
    var poppinsItalic:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_ltalic)
    var poppinsLight:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_light)
    var poppinsMedium:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_medium)
    var poppinsRegular:Typeface? = ResourcesCompat.getFont(context, R.font.poppins_regular)
}