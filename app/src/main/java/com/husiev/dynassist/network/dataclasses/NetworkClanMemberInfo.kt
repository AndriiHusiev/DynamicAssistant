package com.husiev.dynassist.network.dataclasses

import com.husiev.dynassist.components.main.utils.NO_DATA
import com.husiev.dynassist.database.entity.ClanEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccountClanData(
	@SerialName(value = "account_id")
	val accountId: Int,
	@SerialName(value = "role_i18n")
	val roleLocalized: String,
	val role: String,
	@SerialName(value = "joined_at")
	val joinedAt: Int,
	@SerialName(value = "account_name")
	val accountName: String,
	
	val clan: ClanShortInfo
)

@Serializable
data class ClanShortInfo(
	@SerialName(value = "clan_id")
	val clanId: Int,
	@SerialName(value = "members_count")
	val membersCount: Int,
	@SerialName(value = "created_at")
	val createdAt: Int,
	val name: String,
	val tag: String,
	val color: String,
	
	val emblems: ClanEmblems
)

@Serializable
data class ClanEmblems(
	val x24: ClanEmblemsLinks,
	val x32: ClanEmblemsLinks,
	val x64: ClanEmblemsLinks,
	val x195: ClanEmblemsLinks,
	val x256: ClanEmblemsLinks,
)

@Serializable
data class ClanEmblemsLinks(
	val portal: String? = null,
	val wot: String? = null,
	val wowp: String? = null,
)

fun NetworkAccountClanData.asEntity() = ClanEntity(
	accountId = accountId,
	joinedAt = joinedAt,
	roleLocalized = roleLocalized,
	clanId = clan.clanId,
	createdAt = clan.createdAt,
	membersCount = clan.membersCount,
	name = clan.name,
	tag = clan.tag,
	color = clan.color,
	emblem = clan.emblems.x195.portal ?: NO_DATA,
)