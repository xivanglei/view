package xl.gcs.com.wheelview;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xianglei on 2018/3/22.
 */

public class ProvinceData {

    @SerializedName("name")
    public String provinceName;

    public CityData[] city;

    public class CityData {

        @SerializedName("name")
        public String cityName;

        public String[] area;
    }
}
