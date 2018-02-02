#Giphy Viewer

Giphy viewer is a gif viewer that uses the public GIPHY API.

It retrieves the different needed information via the [GIPHY API](https://developers.giphy.com/docs/).
The implementation consist in an activity with a ViewPager holding two fragments. 


The first fragment displays by default a list of the trending gifs in GIPHY website. A search bar is included in the top in order to search items given a word or phrase introduced by the user.
The second fragment contains a grid of gifs that have been marked as favourite by the user. The corresponding RecyclerView is linked to an ORMLite to store the favorite gifs.
All items displayed in both fragments contains a button to add/remove gifs to/from favourites.

Implementation details:

- Applies Dependency injection with [Dagger2](https://google.github.io/dagger/)
- [Otto](http://square.github.io/otto/) is used to decouple Interactors from Presenters
- Server is attacked with [Retrofit2](http://square.github.io/retrofit/)
- [ButterKnife](http://jakewharton.github.io/butterknife/) is used to find the corresponding views
- [Glide](https://github.com/bumptech/glide) is used for image caching and animated gif displaying
- Two flavors were added: mock & prod (@see app/build.gradle) in order to be able to build an app which aims to a mock server (@see MockGiphyManager) and to a mock datastore (@see MockSQLiteGifDataStore)




**MVP branch**

Implements an [MVP](https://github.com/googlesamples/android-architecture) pattern using concepts of the [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html).

**Master Branch** 

(Work in progress...)
I am currently migrating the code to an MVVM pattern using the new [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html):
- [Room](https://developer.android.com/topic/libraries/architecture/room.html)
- [Livedata](https://developer.android.com/topic/libraries/architecture/livedata.html)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)





![Alt Text](http://awoisoak.com/public/android/giphy-viewer-animation.gif)

