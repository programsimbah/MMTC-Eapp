package com.pengembangsebelah.stmmappxo;


import androidx.appcompat.app.AppCompatActivity;

public class TestAct extends AppCompatActivity {
//
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.playTrigger)
//    ImageButton trigger;
//
//    @BindView(R.id.listview)
//    ListView listView;
//
//    @BindView(R.id.name)
//    TextView textView;
//
//    @BindView(R.id.sub_player)
//    View subPlayer;
//
//    RadioManager radioManager;
//
//    String streamURL;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_test);
//
//       ButterKnife.bind(this);
//
//        setSupportActionBar(toolbar);
//
//        radioManager = RadioManager.with(this);
//
//        listView.setAdapter(new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(this)));
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//
//        EventBus.getDefault().unregister(this);
//
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        radioManager.unbind();
//
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        radioManager.bind();
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        finish();
//    }
//
//    @Subscribe
//    public void onEvent(String status){
//        switch (status){
//
//            case PlaybackStatus.LOADING:
//
//                // loading
//
//                break;
//
//            case PlaybackStatus.ERROR:
//
//                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();
//
//                break;
//
//        }
//
//        trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
//                ? R.drawable.ic_stop_white_24dp
//                : R.drawable.ic_play_arrow_white_24dp);
//
//    }
//
//    @OnClick(R.id.playTrigger)
//    public void onClicked(){
//
//        if(TextUtils.isEmpty(streamURL)) return;
//
//        radioManager.playOrPause(streamURL);
//    }
//
//    @OnItemClick(R.id.listview)
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//
//        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
//        if(shoutcast == null){
//
//            return;
//
//        }
//
//        textView.setText(shoutcast.getName());
//
//        subPlayer.setVisibility(View.VISIBLE);
//
//        streamURL = shoutcast.getUrl();
//
//        radioManager.playOrPause(streamURL);
//    }
}