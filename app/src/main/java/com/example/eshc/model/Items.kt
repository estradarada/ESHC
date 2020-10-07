package com.example.eshc.model

import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable


data class Items(
  val item_id: String ="",
  val objectName: String ="",
  val objectPhone: String ="",
  val mobilePhone: String ="",
  val kurator: String ="",
  val worker08: String ="",
  val address: String ="",
  @ServerTimestamp
  val serverTimestamp: String =""
  ): Serializable