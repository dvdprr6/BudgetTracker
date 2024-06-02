package com.api.service

import com.api.models.{CategoryGroupByWithTotalsDto, CategoryGroupByWithTotalsEntity, PostgresConnectionDto}
import com.api.repository.CategoryRepository
import com.api.utils.Utils
import zio.{Task, ZLayer}

trait CategoryService{
  def getCategoryGroupByWithTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsDto]]
}

class CategoryServiceImpl(categoryRepository: CategoryRepository) extends CategoryService {

  override def getCategoryGroupByWithTotals()(implicit postgresConnectionDto: PostgresConnectionDto): Task[Seq[CategoryGroupByWithTotalsDto]] = {
    val categoryGroupByWithTotalsDtoRecords =
      for{
        categoryGroupByWithTotalsEntity <- categoryRepository.getWithGroupByTotals()
        categoryGroupByWithTotalsDto = categoryGroupByWithTotalsEntity.map(record => toCategoryGroupByWithTotalsDto(record))
      }  yield categoryGroupByWithTotalsDto

    categoryGroupByWithTotalsDtoRecords
  }

  private def toCategoryGroupByWithTotalsDto(categoryGroupByWithTotalsEntity: CategoryGroupByWithTotalsEntity): CategoryGroupByWithTotalsDto = {
    val id = categoryGroupByWithTotalsEntity.id
    val categoryName = categoryGroupByWithTotalsEntity.categoryName
    val total = categoryGroupByWithTotalsEntity.total
    val createDate = Utils.localDateTimeToString(categoryGroupByWithTotalsEntity.createDate)
    val modifiedDate = Utils.localDateTimeToString(categoryGroupByWithTotalsEntity.modifiedDate)

    CategoryGroupByWithTotalsDto(id, categoryName, total, createDate, modifiedDate)
  }
}

object CategoryServiceImpl{
  private def apply(categoryRepository: CategoryRepository) =
    new CategoryServiceImpl(categoryRepository)

  lazy val live: ZLayer[CategoryRepository, Throwable, CategoryService] =
    ZLayer.fromFunction(apply _)
}