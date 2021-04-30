package com.skillbox.homework32_5

class Repository {

    fun getProducts(): List<Product> {
        return listOf(
               Product("JBL Tune 120 TWS, белый",
               60,
               "https://avatars.mds.yandex.net/get-mpic/4080439/img_id2608411432412268651.jpeg/orig"),
                Product("Urbanears Plattan 2 Bluetooth, black",
                30,
                "https://avatars.mds.yandex.net/get-mpic/200316/img_id3749543741264197089.png/orig"),
                 Product("JBL Live 400BT, white",
                 50,
                 "https://avatars.mds.yandex.net/get-mpic/1642819/img_id2463122459034362489.jpeg/orig"),
            Product("JBL Tune 750BTNC, blue",
            70,
            "https://avatars.mds.yandex.net/get-mpic/4080967/img_id1374397078874541125.png/orig")
        )
    }
}