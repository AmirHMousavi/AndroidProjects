/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lnureader.database.ChannelDataSource;
import com.example.lnureader.dummy.DummyContent;
import com.example.lnureader.rssstuff.ChannelInfo;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ChannelListFragment extends ListFragment {

	private final Integer FRAGMENT_GROUP_ID = 111;
	private ChannelDataSource datasource;
	private List<ChannelInfo> channelsList;

	private ArrayAdapter<ChannelInfo> channelsAdapter;
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types of parameters
	public static ChannelListFragment newInstance(String param1, String param2) {
		ChannelListFragment fragment = new ChannelListFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ChannelListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mMessageReceiver, new IntentFilter("REFRESH_CHANNELS_LIST"));

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

		datasource = new ChannelDataSource(getActivity());
		datasource.open();
		datasource.sortByName();
		channelsList = datasource.getAllChannels();

		channelsAdapter = new myChannelAdapter(getActivity(), channelsList);
		setListAdapter(channelsAdapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle(channelsList.get(info.position).getChannelName()
				.toString());
		menu.add(FRAGMENT_GROUP_ID, 0, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getGroupId() == FRAGMENT_GROUP_ID) {
			if (item.getItemId() == 0) {
				ChannelInfo channel = channelsList.get(info.position);
				datasource.deleteChannel(channel);
				datasource.sortByName();
				channelsAdapter.remove(channel);
				channelsAdapter.notifyDataSetChanged();
			}
			return true;
		}
		return false;

	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("channels list", "broadcast is recived");
			System.out.println(intent.getStringExtra("msg"));
			datasource.sortByName();
			channelsList = datasource.getAllChannels();
			channelsAdapter.add(channelsList.get(channelsList.size() - 1));
			channelsAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener
					.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
		}

		// creating the intent to call the NewsFeeds activity
		// sending the channel link and name
		String[] url_name = (String[]) v.getTag();
		Intent intent = new Intent(getActivity(), NewsFeeds.class);
		intent.putExtra("channelLink", url_name[0]);
		intent.putExtra("channelName", url_name[1]);
		startActivity(intent);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(String id);
	}

	private class myChannelAdapter extends ArrayAdapter<ChannelInfo> {
		private final LayoutInflater inflater;

		public myChannelAdapter(Activity activity,
				List<ChannelInfo> channelsList) {
			super(activity, R.layout.itemview_channel, channelsList);
			this.inflater = activity.getLayoutInflater();

			// **************************************
			for (int i = 0; i < channelsList.size(); i++)
				System.out.println(channelsList.get(i).getChannelName());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.itemview_channel,
						parent, false);
			}

			ChannelInfo currentItem = channelsList.get(position);
			String _name = currentItem.getChannelName();
			String _description = currentItem.getDescription();
			String _link = currentItem.getChannelURL();
			String _date = currentItem.getLastModification();
			Bitmap _logo = new ImageUtils().byteToBitmap(currentItem
					.getLogoImage());


			TextView name = (TextView) convertView
					.findViewById(R.id.channel_name);
			name.setText(_name);
			TextView description = (TextView) convertView
					.findViewById(R.id.channel_description);
			description.setText(_description);
//			TextView date = (TextView) convertView
//					.findViewById(R.id.modification_date);
//			date.setText(_date);
			ImageView logo = (ImageView) convertView
					.findViewById(R.id.channel_logo);
			logo.setImageBitmap(_logo);
			String[] tag = new String[] { _link, _name };
			convertView.setTag(tag);
			return convertView;
		}
	}

}
