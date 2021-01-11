package com.mypackage.naengbiseo.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mypackage.naengbiseo.FoodIcon
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.adapter.ShoppingCartViewAdapter
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.FoodAddViewModel
import com.mypackage.naengbiseo.viewmodel.FoodAddViewModelFactory
import kotlinx.android.synthetic.main.fragment_food_icon_add.*
import kotlinx.android.synthetic.main.fragment_food_icon_add.search_recyclerview_shopping_cart

class FoodIconAddFragment : Fragment() {
    var storeWay:String = ""
    var useDate:String = ""
    var treatWay:String = ""
    var selectList = mutableListOf<Boolean>()
    private val TYPE_CATEGORY_HEADER = 0
    private val TYPE_CATEGORY_FOOTER = 1
    companion object { // static 변수처럼 사용됨. 앱이 실행될 때 딱 한 번 초기화
        private val TYPE_CATEGORY_HEADER = 0
        private val TYPE_CATEGORY_FOOTER = 1
        private val allIconList = mutableListOf<FoodIcon>(
            FoodIcon("직접입력", TYPE_CATEGORY_HEADER),
            FoodIcon("육류", R.drawable.meat , "육류"),
            FoodIcon("과일류", R.drawable.fruit, "과일류"),
            FoodIcon("채소류", R.drawable.vegetable, "채소류"),
            FoodIcon("해산물", R.drawable.seafood, "해산물"),
            FoodIcon("곡류/견과류", R.drawable.nuts , "곡류/견과류"),
            FoodIcon("유제품/난류", R.drawable.dairy,"유제품/난류"),
            FoodIcon("면류", R.drawable.noodle,"면류"),
            FoodIcon("통조림", R.drawable.can,"통조림"),
            FoodIcon("냉동식품", R.drawable.frozen_food,"냉동식품"),
            FoodIcon("간편조리식품", R.drawable.convenience_food, "간편조리식품"),
            FoodIcon("가열조리식품", R.drawable.boiling_food,"가열조리식품"),
            FoodIcon("잼/양념/오일류", R.drawable.jam_seasoning_oil,"잼/양념/오일류"),
            FoodIcon("음료/제과류", R.drawable.beverage_snack,"음료/제과류"),
            FoodIcon("건강식품", R.drawable.health_food,"건강식품"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("육류", TYPE_CATEGORY_HEADER),
            FoodIcon("돼지고기/소고기", R.drawable.pork_beaf,"육류"),
            FoodIcon("콩고기", R.drawable.soybean_meat,"육류"),
            FoodIcon("닭고기", R.drawable.chicken,"육류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("과일류", TYPE_CATEGORY_HEADER),
            FoodIcon("포도", R.drawable.grape,"과일류"),
            FoodIcon("딸기", R.drawable.strawberry,"과일류"),
            FoodIcon("배", R.drawable.pear,"과일류"),
            FoodIcon("귤", R.drawable.mandarin,"과일류"),
            FoodIcon("사과", R.drawable.apple,"과일류"),
            FoodIcon("복숭아", R.drawable.peach,"과일류"),
            FoodIcon("바나나", R.drawable.banana,"과일류"),
            FoodIcon("수박", R.drawable.watermelon,"과일류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("채소류", TYPE_CATEGORY_HEADER),
            FoodIcon("브로콜리", R.drawable.broccoli,"채소류"),
            FoodIcon("호박", R.drawable.pumpkin,"채소류"),
            FoodIcon("토마토", R.drawable.tomato,"채소류"),
            FoodIcon("양파", R.drawable.onion,"채소류"),
            FoodIcon("가지", R.drawable.eggplant,"채소류"),
            FoodIcon("피망", R.drawable.pimento,"채소류"),
            FoodIcon("당근", R.drawable.carrot,"채소류"),
            FoodIcon("파", R.drawable.pa,"채소류"),
            FoodIcon("무", R.drawable.moo,"채소류"),
            FoodIcon("버섯", R.drawable.mushroom,"채소류"),
            FoodIcon("고구마", R.drawable.sweet_potato,"채소류"),
            FoodIcon("감자", R.drawable.potato,"채소류"),
            FoodIcon("옥수수", R.drawable.corn,"채소류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("해산물", TYPE_CATEGORY_HEADER),
            FoodIcon("생선", R.drawable.fish,"해산물"),
            FoodIcon("조개", R.drawable.clam,"해산물"),
            FoodIcon("미역", R.drawable.seaweed,"해산물"),
            FoodIcon("새우", R.drawable.shrimp,"해산물"),
            FoodIcon("문어", R.drawable.octopus,"해산물"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("곡류/견과류", TYPE_CATEGORY_HEADER),
            FoodIcon("콩", R.drawable.bean,"곡류/견과류"),
            FoodIcon("쌀", R.drawable.rice,"곡류/견과류"),
            FoodIcon("땅콩", R.drawable.peanut,"곡류/견과류"),
            FoodIcon("밤", R.drawable.chestnut,"곡류/견과류"),
            FoodIcon("아몬드", R.drawable.almond,"곡류/견과류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("유제품/난류", TYPE_CATEGORY_HEADER),
            FoodIcon("요거트", R.drawable.yogurt,"유제품/난류"),
            FoodIcon("치즈", R.drawable.cheese,"유제품/난류"),
            FoodIcon("우유", R.drawable.milk,"유제품/난류"),
            FoodIcon("두유", R.drawable.dooyou,"유제품/난류"),
            FoodIcon("버터", R.drawable.butter,"유제품/난류"),
            FoodIcon("달걀", R.drawable.egg,"유제품/난류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("면류", TYPE_CATEGORY_HEADER),
            FoodIcon("라면사리", R.drawable.ramen_noodle,"면류"),
            FoodIcon("일반면", R.drawable.ordinary_noodle,"면류"),
            FoodIcon("스파게티면", R.drawable.spaghetti_noodle,"면류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("잼/양념/오일류", TYPE_CATEGORY_HEADER),
            FoodIcon("마요네즈", R.drawable.mayonnaise,"잼/양념/오일류"),
            FoodIcon("케챱", R.drawable.ketchup,"잼/양념/오일류"),
            FoodIcon("올리브유", R.drawable.olive_oil,"잼/양념/오일류"),
            FoodIcon("밀가루", R.drawable.flour,"잼/양념/오일류"),
            FoodIcon("설탕", R.drawable.sugar,"잼/양념/오일류"),
            FoodIcon("소금", R.drawable.salt,"잼/양념/오일류"),
            FoodIcon("간장", R.drawable.ganjang,"잼/양념/오일류"),
            FoodIcon("잼", R.drawable.jam,"잼/양념/오일류"),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER)
        )
        private val iconList = mutableListOf<FoodIcon>()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_icon_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val viewModel: FoodAddViewModel by viewModels(ownerProducer = { requireParentFragment() })
        val ac=activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = FoodAddViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            FoodAddViewModel::class.java)

        viewModel.allExcelData.observe(viewLifecycleOwner, Observer {
        })

        for (i in 0..allIconList.size) {
            selectList.add(false)
        }

        search_recyclerview_shopping_cart.adapter = ShoppingCartViewAdapter(allIconList)

        (search_recyclerview_shopping_cart.adapter as ShoppingCartViewAdapter).setItemClickListener(
            object : ShoppingCartViewAdapter.OnItemClickListener {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onClick(v: View, position: Int) {
                    if (selectList[position] == false) {
                        if (findTrue()) {
                            Toast.makeText(
                                activity as MainActivity,
                                "이미 재료를 선택하셨습니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        storeWay = viewModel.getExcelData(allIconList[position].iconName)!!.first
                        useDate = viewModel.getExcelData(allIconList[position].iconName)!!.second
                        treatWay = viewModel.getExcelData(allIconList[position].iconName)!!.third
                        viewModel.setIcon(allIconList[position].iconResource,allIconList[position].iconName,allIconList[position].category,storeWay,useDate,treatWay)
                        v.elevation = 10F
                        selectList[position] = true
                    } else {
                        v.elevation = 0F
                        selectList[position] = false
                    }
                }
            })

        // 열을 3으로 설정한 GridLayoutManager 의 인스턴스를 생성하고 설정
        val gridLayoutManager = GridLayoutManager(activity, 4)

        // SpanSizeLookup 으로 위치별로 차지할 폭을 결정한다
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 카페고리 header 나 footer 영역은 grid 4칸을 전부 차지하여 표시
                if (allIconList[position].getIconResource == TYPE_CATEGORY_HEADER ||
                    allIconList[position].getIconResource == TYPE_CATEGORY_FOOTER
                ) {
                    return gridLayoutManager.spanCount
                }
                return 1 // 나머지는 1칸만 사용
            }
        }
        //레이아웃 매니저 추가
        search_recyclerview_shopping_cart.layoutManager = gridLayoutManager

        back_button.setOnClickListener {
            findNavController().navigateUp()
        }

        add_food_icon_btn.setOnClickListener{
            if(!findTrue()){
                Toast.makeText(
                    activity as MainActivity,
                    "추가할 재료를 선택해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            else findNavController().navigate(R.id.action_foodIconAddFragment_to_foodAddFragment)
        }
    }



    fun findTrue(): Boolean {
        var flag = false
        for (b in selectList) {
            if (b == true) flag = true
        }
        return flag
    }
}