package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.BannerDao
import com.swamisamarthpet.data.model.Banner
import com.swamisamarthpet.data.tables.BannersTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList
import java.util.zip.Deflater

class BannerRepo: BannerDao {

    override suspend fun insertBanner(multiPartData: MultiPartData): Int {
        var image: String
        multiPartData.forEachPart { part->
            if(part is PartData.FileItem){
                val file = File("./build/resources/main/static/banner.png")

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
                    image = bos.toByteArray().contentToString()
                }
                DatabaseFactory.dbQuery {
                    BannersTable.insert { banner ->
                        banner[bannerImage] = image
                    }
                }
            }
            part.dispose()
        }
        return 1
    }

    override suspend fun updateBanner(bannerId: Int, multiPartData: MultiPartData): Int {
        var image: String
        multiPartData.forEachPart { part->
            if(part is PartData.FileItem){
                val file = File("./build/resources/main/static/banner.png")

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
                    image = bos.toByteArray().contentToString()
                }
                DatabaseFactory.dbQuery {
                    BannersTable.update({
                        BannersTable.bannerId.eq(bannerId)
                    }){ statement->
                        statement[bannerImage] = image
                    }
                }
            }
            part.dispose()
        }
        return 1
    }

    override suspend fun deleteBanner(bannerId: Int): Int {
        return DatabaseFactory.dbQuery {
            BannersTable.deleteWhere { BannersTable.bannerId.eq(bannerId) }
        }
    }

    override suspend fun getAllBanners(): List<Banner> {
        return DatabaseFactory.dbQuery{
            BannersTable.selectAll().mapNotNull {
                rowToBanner(it)
            }
        }
    }

    private fun rowToBanner(row: ResultRow?): Banner? {
        if(row == null)
            return null

        return Banner(
            bannerId = row[BannersTable.bannerId],
            bannerImage = row[BannersTable.bannerImage]
        )

    }

}