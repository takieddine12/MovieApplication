package taki.eddine.myapplication.PaginationUi.pagination.filterpagination

//class FilterViewModel : ViewModel() {
//    var filterTextAll = MutableLiveData<String>()
//    var listLiveData: LiveData<PagedList<MoviesDataModel>>? = null
//    private val executor: Executor = Executors.newFixedThreadPool(5)
//    val data: LiveData<PagedList<MoviesDataModel>>
//        get() {
//            listLiveData = Transformations.switchMap(filterTextAll) { input ->
//                val myDataSourceFactory = MyDataSourceFactory(input, executor)
//                val config = PagedList.Config.Builder()
//                        .setPageSize(10)
//                        .build()
//                LivePagedListBuilder<Int, MoviesDataModel>(myDataSourceFactory, config)
//                        .setFetchExecutor(executor)
//                        .build()
//            }
//            return listLiveData!!
//        }
//}