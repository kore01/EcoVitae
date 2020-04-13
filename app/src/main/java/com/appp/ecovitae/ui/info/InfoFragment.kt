package com.appp.ecovitae.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.appp.ecovitae.Adapter.CustomAdapter
import com.appp.ecovitae.Adapter.InfoWindowAdapter
import com.appp.ecovitae.CheckBoxList
import com.appp.ecovitae.DataModel.Accounts.MyAccount
import com.appp.ecovitae.DataModel.InfoWindowData
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.appp.ecovitae.R.*
import com.appp.ecovitae.ui.send.ShopsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import com.ms.square.android.expandabletextview.ExpandableTextView
import java.util.ArrayList
import java.util.Observer


class InfoFragment : Fragment(){


    private lateinit var sendViewModel: ShopsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(ShopsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_info, container, false)
       // val textView: TextView = root.findViewById(R.id.text_send)


        val zashto: ExpandableTextView = root.findViewById(R.id.zashto)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            zashto.setText(Html.fromHtml("<ul>\n" +
                    "  <li>Спестяваме природни ресурси.</li>\n" +
                    "  <li>Намаляваме депонираните отпадъци в депата за отпадъци.</li>\n" +
                    "  <li>Грижа за здравето на всеки човек.</li>\n" +
                    "  <li>Всеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.</li>\n" +
                    "  <li>Осигуряваме по-добро бъдеще за нашите деца.</li>\n" +
                    "</ul>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            zashto.setText(Html.fromHtml("<ul>\n" +
                    "  <li>Спестяваме природни ресурси.</li>\n" +
                    "  <li>Намаляваме депонираните отпадъци в депата за отпадъци.</li>\n" +
                    "  <li>Грижа за здравето на всеки човек.</li>\n" +
                    "  <li>Всеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.</li>\n" +
                    "  <li>Осигуряваме по-добро бъдеще за нашите деца.</li>\n" +
                    "</ul>"));
        }

        val kakvo_h: ExpandableTextView = root.findViewById(R.id.kakvo_h)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            kakvo_h.setText(Html.fromHtml(
                    "                </h2> <ol class=\"big-ol\"> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                    "                                В съдове за пластмаса или хартия изхвърляйте:\n" +
                    "                            </h5> <ul class=\"check-list\"> <li>\n" +
                    "                                        Вестници и списания;\n" +
                    "                                    </li> <li>\n" +
                    "                                        Празни кутии от сокове и мляко;\n" +
                    "                                    </li> <li>\n" +
                    "                                        Кашони и други хартиени и картонени опаковки;\n" +
                    "                                    </li> <li>\n" +
                    "                                        Картонени кутии от сладки, бонбони, яйца, обувки и др.;\n" +
                    "                                    </li> <li>\n" +
                    "                                        Хартиени торбички и чували.\n" +
                    "                                    </li> </ul> </li> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                    "                                В съдовете за разделно събиране на хартия не се допуска изхвърлянето на:\n" +
                    "                            </h5> <ul class=\"check-list\"> <li>\n" +
                    "                                        Битови отпадъци (например салфетки, санитарни материали, угарки);\n" +
                    "                                    </li> <li>\n" +
                    "                                        Силно замърсени или омаслени хартиени и картонени отпадъци (напр. мазна кутия от пица);\n" +
                    "                                    </li> <li>\n" +
                    "                                        Хартии с нанесени специални покрития (напр. метални и пластмасови фолиа);\n" +
                    "                                    </li> <li>\n" +
                    "                                        Всякакви други отпадъци, които не са хартиени.\n" +
                    "                                    </li> </ul> </li> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                    "                                Кутиите и кашоните трябва да са предварително сгънати.\n" +
                    "                            </h5> <ul class=\"check-list\"> </ul> </li> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                    "                                Избягвай изхвърлянето в съдовете за разделно събиране на големи опаковки от велпапе и картон.\n" +
                    "                            </h5> <p>\n" +
                    "                       ", Html.FROM_HTML_MODE_COMPACT));
        } else {
            kakvo_h.setText(Html.fromHtml("<ul>\n" +
                    "  <li>Спестяваме природни ресурси.</li>\n" +
                    "  <li>Намаляваме депонираните отпадъци в депата за отпадъци.</li>\n" +
                    "  <li>Грижа за здравето на всеки човек.</li>\n" +
                    "  <li>Всеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.</li>\n" +
                    "  <li>Осигуряваме по-добро бъдеще за нашите деца.</li>\n" +
                    "</ul>"));
        }



        val kakvo_p: ExpandableTextView = root.findViewById(R.id.kakvo_p)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            kakvo_p.setText(Html.fromHtml(
                "                </h2> <ol class=\"big-ol\"> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                        "                              Изхвърляй в предоставения съд само пластмасови отпадъци от опаковки, като например:\n" +
                        "                            </h5> <ul class=\"check-list\"> <li>\n" +
                        "                                        Пластмасови бутилки от вода, безалкохолни, олио и бира;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Кофички от кисело мляко и пластмасови бутилки от прясно мляко;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Туби и шишета от козметика и санитарни продукти;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Пластмасови чаши и тарелки /почистени от остатъците от храна/, палета и каси;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Метални кенчета от напитки и почистени консервени кутии;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Капачки от буркани и бутилки;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Найлонови торбички, стреч фолио и опаковъчно фолио.\n" +
                        "                                    </li> </ul> </li> <li> <h5 style=\"color: #DE9301;\">\n" +
                        "                                В съдовете за разделно събиране на пластмаса не изхвърляй:\n" +
                        "                            </h5> <ul class=\"check-list\"> <li>\n" +
                        "                                        Блистери от лекарства, туби от масла и други вредни вещества;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Домакински електроуреди, авточасти и др.;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Отпадъци, които не са пластмасови.\n" +
                        "                       ", Html.FROM_HTML_MODE_COMPACT));
        } else {
            kakvo_p.setText(Html.fromHtml("<ul>\n" +
                    "  <li>Спестяваме природни ресурси.</li>\n" +
                    "  <li>Намаляваме депонираните отпадъци в депата за отпадъци.</li>\n" +
                    "  <li>Грижа за здравето на всеки човек.</li>\n" +
                    "  <li>Всеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.</li>\n" +
                    "  <li>Осигуряваме по-добро бъдеще за нашите деца.</li>\n" +
                    "</ul>"));
        }

        val kakvo_s: ExpandableTextView = root.findViewById(R.id.kakvo_s)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            kakvo_s.setText(Html.fromHtml(
                "                </h2> <ol class=\"big-ol\"> <li> <h5 style=\"color: #1A8FD2;\">\n" +
                        "                                Изхвърляй в предоставения съд само стъклени отпадъци от опаковки:\n" +
                        "                            </h5> <ul class=\"check-list\"> <li>\n" +
                        "                                        Бутилки от всякакъв вид и цвят;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Стъклени бурканчета и шишета от козметични продукти;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Буркани от всякакви храни.\n" +
                        "                                    </li> </ul> </li> <li> <h5 style=\"color: #37B700;\">\n" +
                        "                                В съдовете за разделно събиране на стъкло не изхвърляй:\n" +
                        "                            </h5> <ul class=\"check-list\"> <li>\n" +
                        "                                        Счупени чаши и чинии;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Порцелан и изделия от огнеупорно стъкло;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Крушки и луминесцентни лампи;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Стъкло от прозорци и огледала;\n" +
                        "                                    </li> <li>\n" +
                        "                                        Автомобилни стъкла.\n" +
                        "                                    </li> </ul> </li> <li> <h5 style=\"color: #37B700;\">\n" +
                        "                       ", Html.FROM_HTML_MODE_COMPACT));
        } else {
            kakvo_s.setText(Html.fromHtml("<ul>\n" +
                    "  <li>Спестяваме природни ресурси.</li>\n" +
                    "  <li>Намаляваме депонираните отпадъци в депата за отпадъци.</li>\n" +
                    "  <li>Грижа за здравето на всеки човек.</li>\n" +
                    "  <li>Всеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.</li>\n" +
                    "  <li>Осигуряваме по-добро бъдеще за нашите деца.</li>\n" +
                    "</ul>"));
        }


        /*zashto.setText("; Спестяваме природни ресурси.\n" +
                "\tНамаляваме депонираните отпадъци в депата за отпадъци.\n" +
                "\tНамаляваме замърсяването на околната среда – по-малко емисии СО2, по-чист въздух, вода и почва;\n" +
                "\tГрижа за здравето на всеки човек.\n" +
                "\tВсеки потребител, закупувайки си опакован продукт, обозначен със знака за разделно събиране, заплаща такса за неговото разделно събиране и рециклиране.\n" +
                "\tОсигуряваме по-добро бъдеще за нашите деца.")
*/




        //kakvo.setText("Expandable Text View is an android library that allows the users to create the text view which can expand and collapse to read the text description. I bet you guys have seen this in a lot of android applications but might not know the name and its purpose. Well, below is a screenshot of the Instagram application on the Play store. This feature saves a lot of space, rather than laying out the huge chunks of information and occupying the entire page we can further use this option and can utilize the space")


        return root
    }

}