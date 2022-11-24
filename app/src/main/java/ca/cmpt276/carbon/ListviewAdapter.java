package ca.cmpt276.carbon;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ca.cmpt276.carbon.model.Achievements;

public class ListviewAdapter extends BaseAdapter {

    private Context context;

    private int intPlayers;
    private int combinedScore;

    private List<Integer> list;

    private EditText totalScore;        // Total score of all players in a session
    private EditText totalPlayers;
    private TextView achievement;       // Display achievement of player
    private final Achievements level;

    private LayoutInflater layoutInflater;

    public ListviewAdapter(Context context, List<Integer> list, EditText totalScore, TextView achievement,
                           int combinedScore, Achievements level, int intPlayers, EditText totalPlayers) {
        this.context = context;
        this.list = list;
        this.achievement = achievement;
        this.totalScore = totalScore;
        this.combinedScore = combinedScore;
        this.level = level;
        this.intPlayers = intPlayers;
        this.totalPlayers = totalPlayers;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // For future use
    public void setAdapterNumPlayers(int players) {
        this.intPlayers = players;
    }
    public void setScoreList(List<Integer> scoreList) {
        this.list = scoreList;
    }

    public int getUpdatedCombinedScore() {
        int updatedCombScore = 0;
        for (int i = 0; i < list.size(); i++) {
            updatedCombScore += (int) list.get(i);
        }
        return updatedCombScore;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        convertView = null;

        if (convertView == null) {

            holder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.listview_adapter, null);
            holder.editText = (EditText) convertView.findViewById(R.id.number);

            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.editText.setTag(position);

            holder.editText.setText(list.get(position).toString());

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        convertView.setFocusable(true);

        int tagPosition = (Integer)holder.editText.getTag();

        holder.editText.setId(tagPosition);

        holder.editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final int pos = holder.editText.getId();
                final EditText etPlayerScore = (EditText) holder.editText;

                if (etPlayerScore.getText().toString().isEmpty()) {
                    list.set(pos, 0);
                }
                else {
                    list.set(pos, Integer.parseInt(etPlayerScore.getText().toString()));
                }

                combinedScore = 0;
                for (int i = 0; i < list.size(); i++) {
                    combinedScore += (int) list.get(i);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }

        });

        return convertView;
    }

}

class ViewHolder {
    EditText editText;
}