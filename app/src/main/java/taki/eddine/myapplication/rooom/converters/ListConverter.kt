package taki.eddine.myapplication.rooom.converters

//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class ListConverter {
//    private var gson = Gson()
//
//    @TypeConverter
//    fun fromListToString(model : List<MoviesDataModel>) : String {
//          return gson.toJson(model)
//    }
//
//    @TypeConverter
//    fun fromStringToList(data : String): List<MoviesDataModel>{
//        val listType = object : TypeToken<List<MoviesDataModel>>() {}.type
//        return gson.fromJson(data, listType)
//    }
//}