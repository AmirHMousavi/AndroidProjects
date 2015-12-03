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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnureader.database.FavoriteFeedsDataSource;
import com.example.lnureader.dummy.DummyContent;
import com.example.lnureader.rssstuff.FavoriteRssItem;
import com.example.lnureader.rssstuff.RssItem;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FavoriteListFragment extends ListFragment {
	private final Integer FRAGMENT_GROUP_ID = 222;
	private FavoriteFeedsDataSource favDataSource;
	private List<FavoriteRssItem> favList;
	private ArrayAdapter<FavoriteRssItem> favoriteAdapater;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types of parameters
	public static FavoriteListFragment newInstance(String param1, String param2) {
		FavoriteListFragment fragment = new FavoriteListFragment();
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
	public FavoriteListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

		favDataSource = new FavoriteFeedsDataSource(getActivity());
		favDataSource.open();
		favList = favDataSource.getAllFavoriteItems();
		favoriteAdapater = new myFavoriteAdapater(getActivity());
		setListAdapter(favoriteAdapater);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
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

		String url = v.getTag().toString();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(FRAGMENT_GROUP_ID, 0, 0, "Delete");
		menu.add(FRAGMENT_GROUP_ID, 1, 0, "share this feed");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getGroupId() == FRAGMENT_GROUP_ID) {
			if (item.getItemId() == 0) { // delete Country
				FavoriteRssItem fitem = favList.get(info.position);
				favDataSource.deleteFavoriteItem(fitem);
				favoriteAdapater.remove(fitem);
				favoriteAdapater.notifyDataSetChanged();
			}

			if (item.getItemId() == 1) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				FavoriteRssItem shareItem=favList.get(info.position);
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareItem.getTitle());
				shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, shareItem.getDescription());
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareItem.getLink());
				PackageManager packageManager = getActivity().getPackageManager();
				List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, 0);
				boolean isIntentSafe = activities.size() > 0;
				if (isIntentSafe) {
					String title = "Share via ...";
					Intent chooser = Intent.createChooser(shareIntent, title);
					startActivity(chooser);
				}else Toast.makeText(getActivity().getApplicationContext(), "No application is responding to action",
						Toast.LENGTH_SHORT).show();
			}
			}
			return super.onContextItemSelected(item);
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

	private class myFavoriteAdapater extends ArrayAdapter<FavoriteRssItem> {

		private  LayoutInflater inflater;

		public myFavoriteAdapater(Activity activity) {
			super(activity, R.layout.itemview_news, favList);
			inflater = activity.getLayoutInflater();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.itemview_news, parent,false);
			}
			FavoriteRssItem currentItem = favList.get(position);

			String _title = currentItem.getTitle();
			String _description = currentItem.getDescription();
			String _date = currentItem.getDate();
			String _link = currentItem.getLink();
			byte[] _image = currentItem.getImage();

			// ******************************
			TextView title = (TextView) convertView
					.findViewById(R.id.title_view);
			title.setText(_title);
			TextView description = (TextView) convertView
					.findViewById(R.id.description_view);
			description.setText(_description);
			TextView date = (TextView) convertView.findViewById(R.id.date_view);
			date.setText(_date);
			ImageView image = (ImageView) convertView
					.findViewById(R.id.imageView1);
			image.setImageBitmap(new ImageUtils().byteToBitmap(_image));
			String tag = _link;
			convertView.setTag(tag);
			return convertView;
		}

	}

}
