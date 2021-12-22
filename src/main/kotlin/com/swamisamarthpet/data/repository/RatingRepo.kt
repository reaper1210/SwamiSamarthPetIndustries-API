package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.RatingDao
import com.swamisamarthpet.data.model.Rating
import com.swamisamarthpet.data.tables.RatingsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class RatingRepo: RatingDao {
    override suspend fun insertRatings(rating: Int): Int {
        val allRatings = RatingRepo().getRatings()[0]
        DatabaseFactory.dbQuery {
            when(rating){
                1->{
                    RatingsTable.update({RatingsTable.ratingId eq 1}) {ratings->
                        ratings[oneStars] = allRatings.oneStars+1
                    }
                }
                2->{
                    RatingsTable.update({RatingsTable.ratingId eq 1}) {ratings->
                        ratings[twoStars] = allRatings.twoStars+1
                    }
                }
                3->{
                    RatingsTable.update({RatingsTable.ratingId eq 1}) {ratings->
                        ratings[threeStars] = allRatings.threeStars+1
                    }
                }
                4->{
                    RatingsTable.update({RatingsTable.ratingId eq 1}) {ratings->
                        ratings[fourStars] = allRatings.fourStars+1
                    }
                }
                5->{
                    RatingsTable.update({RatingsTable.ratingId eq 1}) {ratings->
                        ratings[fiveStars] = allRatings.fiveStars+1
                    }
                }
                else->{
                    return@dbQuery
                }
            }
        }
        return 1
    }

    override suspend fun getRatings(): List<Rating> {
        val result=DatabaseFactory.dbQuery {
            RatingsTable.select { RatingsTable.ratingId eq 1 }.mapNotNull {
                rowToRatings(it)
            }
        }
        return result
    }

    private fun rowToRatings(row: ResultRow?): Rating? {
        if(row == null)
            return null

        return Rating(
            oneStars = row[RatingsTable.oneStars],
            twoStars = row[RatingsTable.twoStars],
            threeStars = row[RatingsTable.threeStars],
            fourStars = row[RatingsTable.fourStars],
            fiveStars = row[RatingsTable.fiveStars]
        )
    }


}