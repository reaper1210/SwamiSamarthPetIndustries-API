package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.PopularDao
import com.swamisamarthpet.data.model.PopularProduct
import com.swamisamarthpet.data.tables.PopularProductsTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList
import java.util.zip.Deflater

class PopularRepo:PopularDao {

    override suspend fun insertPopularProduct(productName: String, multiPart: MultiPartData, productDetails: String, productType: String, productPopularity: Int, productYoutubeVideo: String): Int {
        var productPdf = ""
        val productImages = arrayListOf<String>()
        val multiPartList = arrayListOf<PartData>()
        multiPart.forEachPart {
            if (it is PartData.FileItem) {
                multiPartList.add(it)
            }
        }

        multiPartList.forEachIndexed { index, part ->
            if (part is PartData.FileItem) {
                val file = if (index != 0) {
                    File("./build/resources/main/static/$productName$index.png")
                } else {
                    if(productType=="part"){
                        File("./build/resources/main/static/$productName$index.png")
                    }
                    else{
                        File("./build/resources/main/static/${productName}Pdf.pdf")
                    }
                }

                part.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }

                    //adminAppCode
                    val compressor = Deflater()
                    compressor.setLevel(Deflater.BEST_COMPRESSION)
                    compressor.setInput(file.readBytes())
                    compressor.finish()
                    val bos = ByteArrayOutputStream(file.readBytes().size)
                    val buf = ByteArray(1024)
                    while (!compressor.finished()) {
                        val count = compressor.deflate(buf)
                        bos.write(buf, 0, count)
                    }
                    bos.close()
                    if(file.exists()){
                        file.delete()
                    }

                    if ((index == 0) and (productType=="machine")) {
                        productPdf = bos.toByteArray().contentToString()
                    }
                    else {
                        productImages.add(bos.toByteArray().contentToString())
                    }

                }
                if (index == multiPartList.lastIndex) {
                    DatabaseFactory.dbQuery {
                        PopularProductsTable.insert { product ->
                            product[PopularProductsTable.productName] = productName
                            product[PopularProductsTable.productImages] = productImages.joinToString(";")
                            product[PopularProductsTable.productDetails] = productDetails
                            product[PopularProductsTable.productPdf] = productPdf
                            product[PopularProductsTable.productPopularity] = productPopularity
                            product[PopularProductsTable.productType] = productType
                            product[PopularProductsTable.productYoutubeVideo] = productYoutubeVideo
                        }
                    }
                }
            }
            part.dispose()
        }
        return 1
    }

    override suspend fun deletePopularProduct(productId: Int): Int {
        return DatabaseFactory.dbQuery {
            PopularProductsTable.deleteWhere { PopularProductsTable.productId.eq(productId) }
        }
    }

    override suspend fun updatePopularProduct(productId: Int, multiPart: MultiPartData, productDetails: String, productPopularity: Int, productYoutubeVideo: String): Int {
        val productName = getPopularProductById(productId).productName
        var productPdf = ""
        val machineImages = arrayListOf<String>()
        val multiPartList = arrayListOf<PartData>()
        multiPart.forEachPart {
            if(it is PartData.FileItem){
                multiPartList.add(it)
            }
        }

        multiPartList.forEachIndexed { index, part->
            if(part is PartData.FileItem) {
                val file = if(index!=0){
                    File("./build/resources/main/static/${productName}${index}.png")
                }
                else{
                    File("./build/resources/main/static/${productName}Pdf.pdf")
                }

                part.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }

                    //adminAppCode
                    val compressor = Deflater()
                    compressor.setLevel(Deflater.BEST_COMPRESSION)
                    compressor.setInput(file.readBytes())
                    compressor.finish()
                    val bos = ByteArrayOutputStream(file.readBytes().size)
                    val buf = ByteArray(1024)
                    while (!compressor.finished()) {
                        val count = compressor.deflate(buf)
                        bos.write(buf, 0, count)
                    }
                    bos.close()

                    if(index!=0){
                        machineImages.add(bos.toByteArray().contentToString())
                    }
                    else{
                        productPdf = bos.toByteArray().contentToString()
                    }

                }
                if(index==multiPartList.lastIndex){
                    DatabaseFactory.dbQuery {
                        PopularProductsTable.update({
                            PopularProductsTable.productId.eq(productId)
                        }){ statement ->
                            statement[PopularProductsTable.productImages] = machineImages.joinToString(";")
                            statement[PopularProductsTable.productDetails] = productDetails
                            statement[PopularProductsTable.productPdf] = productPdf
                            statement[PopularProductsTable.productPopularity] = productPopularity
                            statement[PopularProductsTable.productYoutubeVideo] = productYoutubeVideo
                        }
                    }
                }
            }
            part.dispose()
        }

        return 1
    }

    override suspend fun getPopularProductById(productId: Int): PopularProduct {
        return DatabaseFactory.dbQuery {
            PopularProductsTable.select {
                PopularProductsTable.productId.eq(productId)
            }.map {
                rowToPopularProduct(it)
            }.singleOrNull()!!
        }
    }

    override suspend fun getAllPopularProducts(): List<HashMap<String, String>> {

        val machineHashMap = ArrayList<HashMap<String,String>>()
        val rawList = DatabaseFactory.dbQuery{
            PopularProductsTable.selectAll().mapNotNull {
                rowToPopularProduct(it)
            }
        }
        for(rawMachine in rawList){
            val imageList = rawMachine.productImages.split(";")
            val machine = HashMap<String,String>()
            machine["productId"] = rawMachine.productId.toString()
            machine["productName"] = rawMachine.productName
            machine["productImage"] = imageList[0]
            machine["productPopularity"] = rawMachine.productPopularity.toString()
            machine["productType"] = rawMachine.productType
            machineHashMap.add(machine)
        }
        return machineHashMap

    }

    private fun rowToPopularProduct(row: ResultRow?): PopularProduct? {
        if(row == null)
            return null

        return PopularProduct(
            productId = row[PopularProductsTable.productId],
            productName = row[PopularProductsTable.productName],
            productImages = row[PopularProductsTable.productImages],
            productDetails = row[PopularProductsTable.productDetails],
            productPdf = row[PopularProductsTable.productPdf],
            productType= row[PopularProductsTable.productType],
            productPopularity = row[PopularProductsTable.productPopularity],
            productYoutubeVideo = row[PopularProductsTable.productYoutubeVideo]
        )

    }

}
