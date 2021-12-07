package com.software.ddk.dreamgen.utils;

import com.software.ddk.dreamgen.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapValues {

    //rango de deteccion de rarezas.
    //celeste para Ns
    public static final float MIN_N_HUE = 204.0f;
    public static final float MAX_N_HUE = 208.0f;
    //marron para R
    public static final float MIN_R_HUE = 0.0f;
    public static final float MAX_R_HUE = 16.0f;
    //grisaceo para RR
    public static final float MIN_RR_HUE = 235.0f;
    public static final float MAX_RR_HUE = 290.0f;
    //dorado para sr
    public static final float MIN_SR_HUE = 19.125f;
    public static final float MAX_SR_HUE = 33.149998f;
    //nose para mr
    public static final float MIN_MR_HUE = 170.0f;
    public static final float MAX_MR_HUE = 190.0f;

    public static final String jp_uag = "TW96aWxsYS81LjAgKExpbnV4OyBBbmRyb2lkIDQuNC4yOyBHVC1JOTE5NSBCdWlsZC9LT1Q0OUgpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIFZlcnNpb24vNC4wIENocm9tZS8zMC4wLjAuMCBNb2JpbGUgU2FmYXJpLzUzNy4zNi9BTUJJVElPTl9VQS9Hb29nbGVQbGF5L0FuZHJvaWQtL05JWklLQU5PXzJEX3ZlcjM4OkoxZXQ3OEtxRFNoWlI4RUNjSlNRaTBVcWp5SDBiVjdMRkhNSE5LdmxJS2lZMFlyTGFselpCT01yazFpYlp4bzM6Q2ZKM0R0bzhpeGpCc1J5Nldqa0hpSlk2SVcwWG1hSHAwVnV0RkVRU2J5az0=";
    public static final String usa_uag = "TW96aWxsYS81LjAgKExpbnV4OyBBbmRyb2lkIDQuNC4yOyBHVC1JOTE5NSBCdWlsZC9LT1Q0OUgpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIFZlcnNpb24vNC4wIENocm9tZS8zMC4wLjAuMCBNb2JpbGUgU2FmYXJpLzUzNy4zNi9BTUJJVElPTl9VQS9Hb29nbGVQbGF5L0FuZHJvaWQtL05JWklLQU5PXzJEX0dMQl92ZXIzMTo1N0pXUzJKVk95VlBaVUJ4aVJVd05RVFA5akJTaVZMRk1LcXd2MW5XTE5zdlVBektmT21VZW9wZnJrdVY3K09OOktPOHFjQVpORGJUN0tYdS95WTdyZjVZNklXMFhtYUhwMFZ1dEZFUVNieWs9";

    private String[] render_mode = {"R. Rápido","Render HD", "Solo Fondo", "Solo Avatar"};

    private String[][] pupilmap = {
            {"0","0","0","0","0","0","0"}, //none
            {"1","4","7","10","13","16","19"}, //heart
            {"22","25","28","31","34","37","40"}, //sparkle
            {"43","46","49","52","55","58","61"}, //star
            {"64","67","70","73","76","79","82"}, //dollar
            {"85","88","91","94","97","100","103"}, //yen
            {"106","109","112","115","118","121","124"}, //rabbit
            {"127","130","133","136","139","142","145"}, //flower
            {"148","151","154","157","160","163","166"}, //plus
            {"169","172","175","178","181","184","187"}, //cross
            {"190","193","196","199","202","205","208"}, //Music Note
            {"211","214","217","220","223","226","229"}  //cat
    };
    private String[] pupils = {"None","Heart","Sparkle","Star","Dollar","Yen","Rabbit","Flower","Plus","Cross","Music Note","Cat"};
    private int[] pupils_drw = {
            R.drawable.p0,
            R.drawable.p1,
            R.drawable.p22,
            R.drawable.p43,
            R.drawable.p64,
            R.drawable.p85,
            R.drawable.p106,
            R.drawable.p127,
            R.drawable.p148,
            R.drawable.p169,
            R.drawable.p190,
            R.drawable.p211
    };
    private String[] pupil_colors = {"Yellow","Black","Blue","Red","Green","Purple","White"};
    private String[] pupil_tam = {"Small","Regular","Large"};
    private String[] pupil_gradient = {"None","top to bottom","bottom to top","inside to out","outside to int","dulled"};
    private int[] pupil_gradient_drw = {
            R.drawable.gd_0,
            R.drawable.gd_1,
            R.drawable.gd_2,
            R.drawable.gd_3,
            R.drawable.gd_4,
            R.drawable.gd_5
    };
    private String[][] arr_blush = {
            {"Normal", "01"},
            {"Blushing", "11"},
            {"Sweating", "21"},
            {"Blushing and Sweating", "31"}
    };

    private String[][] facemap = {
            {"728","729","730","731","732","733","734","4030","4031","4032","4033","4034","4035","608","609","610","611","612", "15171","15172","15173","15174","15175","15176"}, //gentle
            {"735","736","737","738","739","740","741","4036","4037","4038","4039","4040","4041","613","622","1069","1070","1071", "15177","15178","15179","15180","15181","15182"}, //innocent
            {"742","743","744","745","746","747","748","4042","4043","4044","4045","4046","4047","1072","1073","1074","1075","1076", "15183","15184","15185","15186","15187","15188"}, //cheeky
            {"749","750","751","752","753","754","755","4048","4049","4050","4051","4052","4053","1077","1078","1079","1080","1081", "15189","15190","15191","15192","15193","15194"}, //regular
            {"756","757","758","759","760","761","762","4054","4055","4056","4057","4058","4059","1141","1142","1143","1144","1155", "15195","15196","15197","15198","15199","15200"},  //angleeyed
            {"763","764","765","766","767","768","769","4060","4061","4062","4063","4064","4065","1156","1161","1162","1165","1166", "15201","15202","15203","15204","15205","15206"},  //kind
            {"770","771","772","773","774","775","776","4066","4067","4068","4069","4070","4071","1171","1172","1173","1174","1177", "15207","15208","15209","15210","15211","15212"},  //bumpkin
            {"777","778","779","780","781","782","783","4072","4073","4074","4075","4076","4077","1178","1184","1185","1186","1187", "15213", "15214", "15215", "15216", "15217", "15218"}, //bossy
            {"784","785","786","787","788","789","790","4078","4079","4080","4081","4082","4083","1188","1189","1190","1191","1637", "15219","15220","15221","15222","15223","15224"}, //longing
            {"791","792","793","794","795","796","797","4084","4085","4086","4087","4088","4089","1638","1639","1640","1655","1656", "15225","15226","15227","15228","15229","15230"}, //thin angle-eyed
            {"798","799","800","801","802","803","804","4090","4091","4092","4093","4094","4095","1698","1699","1715","1716","1824", "15231","15232","15233","15234","15235","15236"}, //Cool
            {"805","806","807","808","809","810","811","4096","4097","4098","4099","4100","4101","1955","1956","1959","1960","1961", "15237","15238","15239","15240","15241","15242"}, //staring
            {"812","813","814","815","816","817","818","4102","4103","4104","4105","4106","4107","1962","2241","2242","2243","2381", "15243","15244","15245","15246","15247","15248"}, //loving
            {"819","820","821","822","823","824","825","4108","4109","4110","4111","4112","4113","2382","2383","2384","2385","2386", "15249","15250","15251","15252","15253","15254"}, //western
            {"826","827","828","829","830","831","832","4114","4115","4116","4117","4118","4119","2387","2388","2389","2390","2391", "15255","15256","15257","15258","15259","15260"}, //pure
            {"833","834","835","836","837","838","839","4120","4121","4122","4123","4124","4125","2393","2409","2503","2530","2531", "15261","15262","15263","15264","15265","15266"}, //boyish
            {"840","841","842","843","844","845","846","4126","4127","4128","4129","4130","4131","2532","2533","2534","2535","2547", "15267","15268","15269","15270","15271","15272"}, //Hawk-Eyed
            {"847","848","849","850","851","852","853","4132","4133","4134","4135","4136","4137","2548","3278","3279","3280","3281", "15273","15274","15275","15276","15277","15278"}, //yandere
            {"854","855","856","857","858","859","860","4138","4139","4140","4141","4142","4143","3430","6527","6528","6529","6530", "15279","15280","15281","15282","15283","15284"}, //calm
            {"861","862","863","864","865","866","867","4144","4145","4146","4147","4148","4149","6531","6532","6533","6534","6535", "15285","15286","15287","15288","15289","15290"}, //devilish
            {"2607","2608","2609","2610","2611","2612","4643","4644","4645","4646","4647","4648","4649","6536","6537","6538","6539","6540", "15291","15292","15293","15294","15295","15296"}, //thineyed
            {"2623","2624","2625","2626","2627","2628","2629","4150","4151","4152","4153","4154","4155","6541","6542","6543","6544","6545", "15297","15298","15299","15300","15301","15302"}, //cateyed
            {"3650","3651","3652","3653","3654","3655","3656","4156","4157","4158","4159","4160","4161","6546","6547","6548","6549","6550", "15303","15304","15305","15306","15307","15308"}, //timid
            {"4372","4373","4374","4375","4376","4377","4378","4637","4638","4639","4640","4641","4642","6551","6552","6553","6554","6555", "15309","15310","15311","15312","15313","15314"}, //bright
            {"10187","10188","10189","10190","10191","10192","10193","10194","10195","10196","10197","10198","10199","10200","10201","10202","10203","10204", "15315","15316","15317","15318","15319","15320"}, //narroweyed yandere
            {"10205","10206","10207","10208","10209","10210","10211","10212","10213","10214","10215","10216","10217","10218","10219","10220","10221","10222", "15321","15322","15323","15324","15325","15326"}, //baby
            {"10953","11970","11971","11972","11973","11974","11975","11976","11977","11978","11979","11980","11981","11982","11983","11984","11985","11986", "15327","15328","15329","15330","15331","15332"}, //smiling
            {"10954","11987","11988","11989","11990","11991","11992","11993","11994","11995","11996","11997","11998","11999","12000","12001","12002","12003", "15333","15334","15335","15336","15337","15338"}, //large angle
            {"13129","13130","13131","13132","13133","13134","13135","13136","13137","13138","13139","13140","13141","13142","13143","13144","13145","13146", "15339","15340","15341","15342","15343","15344"}, //droopy pure.
            {"13147","13148","13149","13150","13151","13152","13153","13154","13155","13156","13157","13158","13159","13160","13161","13162","13163","13164", "15345","15346","15347","15348","15349","15350"}, //sanpaku eyed.
            {"14571","14856","14857","14858","14859","14860","14861","14862","14863","14864","14865","14866","14867","14868","14869","14870","14871","14872", "17991","17992","17993","17994","17995","17996"}, //Stylish Eyeshadow
            {"14572","14873","14874","14875","14876","14877","14878","14879","14880","14881","14882","14883","14884","14885","14886","14887","14888","14889", "17997","17998","17999","18000","18001","18002"},  //courtly
            {"21448","21449","21450","21451","21452","21453","21454","21455","21456","21457","21458","21459","21460","21461","21462","21463","21464","21465", "21466","21467","21468","21469","21470","21471"}, //round-eyed face
            {"21424","21425","21426","21427","21428","21429","21430","21431","21432","21433","21434","21435","21436","21437","21438","21439","21440","21441", "21442","21443","21444","21445","21446","21447"} //beastly face
    };
    private String[] arr_ecolor = {"Default","Black","Blue","Red","Green","Purple","Red/Blue","Gold (Premium)","Black (Premium)","Blue (Premium)","Red (Premium)","Green (Premium)","Purple (Premium)","Orange","Pink (Premium)","Amber (Premium)","Aqua (Premium)","Emerald","Yellow-Pink","Pink-Blue","Yellow-Purple","Blue-Purple","Silver-White","Yellow-Green"};
    private String[] arr_eface = {"Gentle","Innocent","Cheeky","Regular","AngleEyed","Kind","Bumpkin","Bossy","Longing","Thin AngleEyed","Cool","Staring","Loving","Western","Pure","Boyish","HawkEyed","Yandere","Calm","Devilish","ThinEyed","CatEyed","Timid","Bright","NarrowEyed Yandere","Baby","Smiling","Large Angle","Droopy Pure","Sanpaku Eyed","Stylish Eyeshadow","Courtly", "Round-Eyed Face", "Beastly Face"};
    //drawables.
    private int[] arr_eface_drw = {
            R.drawable.o_728,
            R.drawable.o_735,
            R.drawable.o_742,
            R.drawable.o_749,
            R.drawable.o_756,
            R.drawable.o_763,
            R.drawable.o_770,
            R.drawable.o_777,
            R.drawable.o_784,
            R.drawable.o_791,
            R.drawable.o_798,
            R.drawable.o_805,
            R.drawable.o_812,
            R.drawable.o_819,
            R.drawable.o_826,
            R.drawable.o_833,
            R.drawable.o_840,
            R.drawable.o_847,
            R.drawable.o_854,
            R.drawable.o_861,
            R.drawable.o_2607,
            R.drawable.o_2623,
            R.drawable.o_3650,
            R.drawable.o_4372,
            R.drawable.o_10187,
            R.drawable.o_10205,
            R.drawable.o_10953,
            R.drawable.o_10954,
            R.drawable.o_13129,
            R.drawable.o_13147,
            R.drawable.o_14571,
            R.drawable.o_14572,
            R.drawable.o_21448,
            R.drawable.o_21424
    };

    private String[][] arr_expressions = {
            {"Regular", "10"},
            {"Smiling", "20"},
            {"Grinning", "30"},
            {"Surprised", "40"},
            {"Angry", "50"},
            {"Sad", "60"},
            {"Staring", "70"},
            {"Relaxed", "80"},
            {"Smirking", "90"},
            {"Winking", "111"},
            {"Furious", "211"},
            {"Embarrassed", "221"},
            {"Blank", "241"},
            {"Longing", "251"},
            {"Oh Dear", "261"},
            {"Deceptive", "271"},
            {"Entranced", "281"},
            {"Dazed", "291"},
            {"Sleepy", "301"},
            {"Smug", "321"},
            {"Troubled", "331"},
            {"Sly", "341"},
            {"Frustrated", "351"},
            {"120", "120"},
            {"130", "130"},
            {"140", "140"}
    };

    private String[][] arr_tam = {{"Regular","11"},{"Short","21"},{"Very Short","31"}};
    private String[][] arr_skin = {{"Regular","11"},{"Pale","21"},{"Tanned","31"}};
    private String[][] cej_color = {
            {"Base", "01"},
            {"Brown","11"},
            {"Gray","21"},
            {"Dark Green","31"},
            {"Amethyst","41"},
            {"Russet","51"},
            {"Puce","61"},
            {"Ash","71"},
            {"Light Brown","81"},
            {"Only Eyeball (dev)","91"}
    };
    private String[][] arr_quality = {{"JPEG","01"},{"PNG","11"}}; //id6

    public MapValues(){}
    public String[][] getPupilmap(){return pupilmap;}
    public String[] getPupils(){return pupils;}
    public String[] getPupil_colors(){return pupil_colors;}
    public String[] getPupil_tam(){return pupil_tam;}
    public String[] getPupil_gradient(){return pupil_gradient;}
    public String[][] getArr_blush(){return arr_blush;}
    public String[][] getFacemap(){return facemap;}
    public String[][] getArr_expressions(){return arr_expressions;}
    public String[][] getArr_tam(){return arr_tam;}
    public String[][] getArr_skin(){return arr_skin;}
    public String[] getArr_eface(){return arr_eface;}
    public String[] getArr_ecolor(){return arr_ecolor;}
    public String[][] getCej_color(){return cej_color;}
    public String[][] getArr_quality(){return arr_quality;}
    public int[] getArr_eface_drw(){return arr_eface_drw;}
    public int[] getPupils_drw(){return  pupils_drw;}
    public int[] getPupil_gradient_drw(){return pupil_gradient_drw;}
    public String[] getRender_mode(){return render_mode;}
}
