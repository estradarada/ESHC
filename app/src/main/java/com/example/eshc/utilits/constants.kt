package com.example.eshc.utilits

import com.example.eshc.MainActivity
import com.example.eshc.adapters.AdapterGuardLate
import com.example.eshc.adapters.FireGuardAdapter
import com.example.eshc.adapters.FireItemAdapter
import com.example.eshc.database.room.ItemRoomDao
import com.example.eshc.database.room.ItemRoomDatabase
import com.example.eshc.database.room.RoomRepository
import com.example.eshc.model.Guards
import com.example.eshc.model.Items
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


const val TAG = "kotlin"

lateinit var APP_ACTIVITY: MainActivity
lateinit var ITEM: Items
lateinit var GUARD: Guards
lateinit var DB: FirebaseFirestore
lateinit var REPOSITORY: RoomRepository
lateinit var ITEM_ROOM_DAO: ItemRoomDao
lateinit var ITEM_ROOM_DATABASE: ItemRoomDatabase
lateinit var optionsItems: FirestoreRecyclerOptions<Items>
lateinit var optionsGuards: FirestoreRecyclerOptions<Guards>
lateinit var adapterFireGuard: FireGuardAdapter<Guards, FireGuardAdapter.GuardViewHolder>
lateinit var adapterFireItem: FireItemAdapter<Items, FireItemAdapter.ItemViewHolder>
lateinit var adapterGuardLate: AdapterGuardLate




val collectionITEMS_REF = FirebaseFirestore.getInstance().collection("Items")
    .orderBy("objectName", Query.Direction.ASCENDING)
val collectionGUARDS_REF = FirebaseFirestore.getInstance().collection("Workers")
    .orderBy("guardName", Query.Direction.ASCENDING)
val collectionSTAFF_REF = FirebaseFirestore.getInstance().collection("Staff")
