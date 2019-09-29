package br.edu.faculdadedelta.projetofirebase.resource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.edu.faculdadedelta.projetofirebase.R;

public class NoticiasProcess extends AsyncTask<Integer, Void, Exception> {
    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<Date> dates;
    ProgressDialog progressDialog;
    Exception exception = null;
    ListView lvRss;
    Context contexto;

    public NoticiasProcess(ListView lvRss, Context contexto) {
        this.progressDialog = new ProgressDialog(contexto);
        this.lvRss = lvRss;
        this.contexto = contexto;

        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        dates = new ArrayList<Date>();
    }

    public InputStream getInputStream(URL url)
    {
        try
        {
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setMessage("Carregando notícias...");
        progressDialog.show();
    }

    @Override
    protected Exception doInBackground(Integer... params) {

        try {
            URL url = new URL("https://www.goiania.go.leg.br/sala-de-imprensa/noticias/RSS");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);

            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(getInputStream(url), "UTF_8");

            boolean insideItem = false;

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    }
                    else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            titles.add(xpp.nextText());
                        }
                    }
                    else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem) {
                            links.add(xpp.nextText());
                        }
                    }
                    else if (xpp.getName().equalsIgnoreCase("dc:date")) {
                        if (insideItem) {
                            try {
                                dates.add(new SimpleDateFormat("dd/MM/yyyy").parse(xpp.nextText().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }

                eventType = xpp.next();
            }
        } catch (MalformedURLException e) {
            exception = e;
        } catch (XmlPullParserException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        }

        return exception;
    }

    @Override
    protected void onPostExecute(Exception s) {
        super.onPostExecute(s);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexto, android.R.layout.simple_list_item_1, titles) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                textView.setTextColor(Color.WHITE);

                return textView;
            }
        };

        lvRss.setAdapter(adapter);
        progressDialog.dismiss();
    }
}