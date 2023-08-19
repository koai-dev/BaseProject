# BaseProject
Base project with many features for android project
# Features
- BaseActivity with funcs to close, open keyboard, add, replace fragment, show, hide loading, send and receive events in app (eventbus), open new activity, map by viewbinding
- BaseFragment is similar to baseActivity
- BaseAction for Fragment, Activity includes getdata, onclick, observer
- BaseApiController with vpn blocking
- BaseApiService (just the parent interface for later implementation)
- BaseListAdapter with listener to handle click event, observer for recycleview tasks in recycleview
- Customview includes loadmore for recycleview, nestedscrollview, moveImageview (like bot in MyViettel app), baseLoadingView
- Utils classes include Screen, Network, SharePreference, Permission, Const, EventType
# How to make it work?
- Fork and clone this project to your computer
- Sync clone project
- From terminal in android studio: paste: /.gradlew assemble
- Get aar file from build folder -> output -> aar -> get base-release.aar
- Import aar file to your project.
- Import implementation("org.greenrobot:eventbus:3.3.1")
- Sync your project. Done
