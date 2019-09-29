package br.edu.faculdadedelta.projetofirebase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.faculdadedelta.projetofirebase.resource.NoticiasProcess;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth fireAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fireDatabase;
    private DatabaseReference fireRef;

    private MaterialButton btnQuestao1;
    private MaterialButton btnQuestao2;
    private MaterialButton btnQuestao3;
    private MaterialButton btnQuestao4;
    private MaterialButton btnQuestao5;
    private MaterialButton btnQuestao6;
    private MaterialButton btnQuestao7;
    private MaterialButton btnQuestao8;
    private MaterialButton btnQuestao9;
    private MaterialButton btnQuestao10;

    private FrameLayout frameQuestao1;
    private FrameLayout frameQuestao2;
    private FrameLayout frameQuestao3;
    private FrameLayout frameQuestao4;
    private FrameLayout frameQuestao5;
    private FrameLayout frameQuestao6;
    private FrameLayout frameQuestao7;
    private FrameLayout frameQuestao8;
    private FrameLayout frameQuestao9;
    private FrameLayout frameQuestao10;
    private FrameLayout frameFinal;
    private FrameLayout frameNoticias;

    private TextView tvUser;
    private TextView tvUserEmail;

    private TextView tvCentroide;
    private TextView tvDireitoide;
    private TextView tvEsquerdoide;
    private TextView tvFinalResult;

    private RadioGroup rgQuestao1;
    private RadioGroup rgQuestao2;
    private RadioGroup rgQuestao3;
    private RadioGroup rgQuestao4;
    private RadioGroup rgQuestao5;
    private RadioGroup rgQuestao6;
    private RadioGroup rgQuestao7;
    private RadioGroup rgQuestao8;
    private RadioGroup rgQuestao9;
    private RadioGroup rgQuestao10;

    private final int QTDEQUESTOES = 10;

    private float pontosDiscordoTotalmente = 0;
    private float pontosConcordoUmPouco = 0;
    private float pontosConcordoTotalmente = 0;

    private float percConcordoTotal = 0;
    private float percConcordoPouco = 0;
    private float percDiscordoTotalmente = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireDatabase = FirebaseDatabase.getInstance();
        fireRef = fireDatabase.getReference();

        fireAuth = FirebaseAuth.getInstance();
        fireUser = fireAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        setCamposAposInflate();

        return true;
    }

    private void setCamposAposInflate() {
        tvUser = findViewById(R.id.tvUserMenu);
        tvUser.setText(fireUser.getDisplayName());
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserEmail.setText(fireUser.getEmail());

        tvCentroide = findViewById(R.id.tvCentroide);
        tvDireitoide = findViewById(R.id.tvDireitoide);
        tvEsquerdoide = findViewById(R.id.tvEsquerdoide);
        tvFinalResult = findViewById(R.id.tvFinalResult);

        frameQuestao1 = findViewById(R.id.frameUm);
        frameQuestao2 = findViewById(R.id.frameDois);
        frameQuestao3 = findViewById(R.id.frameTres);
        frameQuestao4 = findViewById(R.id.frameQuatro);
        frameQuestao5 = findViewById(R.id.frameCinco);
        frameQuestao6 = findViewById(R.id.frameSeis);
        frameQuestao7 = findViewById(R.id.frameSete);
        frameQuestao8 = findViewById(R.id.frameOito);
        frameQuestao9 = findViewById(R.id.frameNove);
        frameQuestao10 = findViewById(R.id.frameDez);

        frameFinal = findViewById(R.id.frameFinal);
        frameNoticias = findViewById(R.id.frameNoticias);

        rgQuestao1 = findViewById(R.id.radioQuestao1);
        rgQuestao2 = findViewById(R.id.radioQuestao2);
        rgQuestao3 = findViewById(R.id.radioQuestao3);
        rgQuestao4 = findViewById(R.id.radioQuestao4);
        rgQuestao5 = findViewById(R.id.radioQuestao5);
        rgQuestao6 = findViewById(R.id.radioQuestao6);
        rgQuestao7 = findViewById(R.id.radioQuestao7);
        rgQuestao8 = findViewById(R.id.radioQuestao8);
        rgQuestao9 = findViewById(R.id.radioQuestao9);
        rgQuestao10 = findViewById(R.id.radioQuestao10);

        btnQuestao1 = findViewById(R.id.btnUm);
        btnQuestao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao1)) {
                    intentFrame(frameQuestao1, frameQuestao2);
                }
            }
        });

        btnQuestao2 = findViewById(R.id.btnDois);
        btnQuestao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao2)) {
                    intentFrame(frameQuestao2, frameQuestao3);
                }
            }
        });

        btnQuestao3 = findViewById(R.id.btnTres);
        btnQuestao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao3)) {
                    intentFrame(frameQuestao3, frameQuestao4);
                }
            }
        });

        btnQuestao4 = findViewById(R.id.btnQuatro);
        btnQuestao4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao4)) {
                    intentFrame(frameQuestao4, frameQuestao5);
                }
            }
        });

        btnQuestao5 = findViewById(R.id.btnCinco);
        btnQuestao5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao5)) {
                    intentFrame(frameQuestao5, frameQuestao6);
                }
            }
        });

        btnQuestao6 = findViewById(R.id.btnSeis);
        btnQuestao6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao6)) {
                    intentFrame(frameQuestao6, frameQuestao7);
                }
            }
        });

        btnQuestao7 = findViewById(R.id.btnSete);
        btnQuestao7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao7)) {
                    intentFrame(frameQuestao7, frameQuestao8);
                }
            }
        });

        btnQuestao8 = findViewById(R.id.btnOito);
        btnQuestao8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao8)) {
                    intentFrame(frameQuestao8, frameQuestao9);
                }
            }
        });

        btnQuestao9 = findViewById(R.id.btnNove);
        btnQuestao9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if (validaQuestao(rgQuestao9)) {
                   intentFrame(frameQuestao9, frameQuestao10);
               }
            }
        });

        btnQuestao10 = findViewById(R.id.btnDez);
        btnQuestao10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaQuestao(rgQuestao10)) {
                    percConcordoTotal = (pontosConcordoTotalmente / QTDEQUESTOES) * 100;
                    percConcordoPouco = (pontosConcordoUmPouco / QTDEQUESTOES) * 100;
                    percDiscordoTotalmente = (pontosDiscordoTotalmente / QTDEQUESTOES) * 100;

                    gravarResultado();
                    resetQuiz();
                    resetPontos();
                    selectFireResultado();
                    intentFrame(frameQuestao10, frameFinal);
                }

            }


        });
    }

    private void resetPontos() {
        this.pontosConcordoUmPouco = 0;
        this.pontosConcordoTotalmente = 0;
        this.pontosDiscordoTotalmente = 0;
    }

    private void popularPoliticalChart() {

        float politicalPercents[] = {percConcordoTotal, percConcordoPouco, percDiscordoTotalmente};
        // Classificações
        String ideologies[] = {"Esquerdoide", "Centroide", "Direitoide"};
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < politicalPercents.length; i++) {
            pieEntries.add(new PieEntry(politicalPercents[i], ideologies[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        PieChart politicalChart = findViewById(R.id.politicalChart);
        data.setValueFormatter(new PercentFormatter(politicalChart));
        politicalChart.setUsePercentValues(true);

        Description desc = new Description();
        desc.setText("TROUXAS EVERYWHERE!");
        desc.setTextColor(Color.WHITE);

        politicalChart.getLegend().setTextColor(Color.WHITE);
        politicalChart.setDescription(desc);
        politicalChart.setData(data);
        politicalChart.animateY(1000);
        politicalChart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_exit) {
            fireAuth.signOut();
            Intent intencao = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intencao);
        } else if (id == R.id.nav_resultado) {
            selectFireResultado();
            esconderFramesQuestoes();
            resetQuiz();
            resetPontos();
            frameFinal.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_noticias) {
            esconderFramesQuestoes();
            new NoticiasProcess((ListView) findViewById(R.id.lvRss), MainActivity.this).execute();
            resetQuiz();
            resetPontos();
            frameNoticias.setVisibility(View.VISIBLE);

        } else if (id == R.id.perguntas) {
            resetQuiz();
            resetPontos();
            frameFinal.setVisibility(View.GONE);
            frameQuestao1.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void esconderFramesQuestoes() {
        frameQuestao1.setVisibility(View.GONE);
        frameQuestao2.setVisibility(View.GONE);
        frameQuestao3.setVisibility(View.GONE);
        frameQuestao4.setVisibility(View.GONE);
        frameQuestao5.setVisibility(View.GONE);
        frameQuestao6.setVisibility(View.GONE);
        frameQuestao7.setVisibility(View.GONE);
        frameQuestao8.setVisibility(View.GONE);
        frameQuestao9.setVisibility(View.GONE);
        frameQuestao10.setVisibility(View.GONE);
        frameFinal.setVisibility(View.GONE);
        frameNoticias.setVisibility(View.GONE);
    }

    private void resetQuiz() {
        this.rgQuestao1.clearCheck();
        this.rgQuestao2.clearCheck();
        this.rgQuestao3.clearCheck();
        this.rgQuestao4.clearCheck();
        this.rgQuestao5.clearCheck();
        this.rgQuestao6.clearCheck();
        this.rgQuestao7.clearCheck();
        this.rgQuestao8.clearCheck();
        this.rgQuestao9.clearCheck();
        this.rgQuestao10.clearCheck();
    }

    private boolean validaQuestao(RadioGroup rg) {
        int checkedRadioButtonId = rg.getCheckedRadioButtonId();
        if (checkedRadioButtonId != -1) {
            if (checkedRadioButtonId == findViewById(R.id.resposta1).getId())
                this.pontosDiscordoTotalmente += 1;
            else if (checkedRadioButtonId == findViewById(R.id.resposta2).getId())
                this.pontosConcordoUmPouco += 1;
            else
                this.pontosConcordoTotalmente += 1;

            return true;
        }

        Toast.makeText(getBaseContext(), "Selecione uma resposta!", Toast.LENGTH_LONG).show();

        return false;
    }

    private void intentFrame(FrameLayout gone, FrameLayout visible) {
        gone.setVisibility(View.GONE);
        visible.setVisibility(View.VISIBLE);
    }

    /* Grava no realtime database a seguinte estrutura(árvore JSON):
    *  usuarios -> uid -> {qtd. questoes, acertos, resultado, porcentagem de acertos}
    *  uid = user id
    *
    *  {
    *       "usuarios": {
    *           "uid001": {
    *               direitoide: x,
    *               centroide: x,
    *               esquerdoide: x
    *           },
    *           "uid002": {...},
    *           "uid003": {...},
    *           "uid004": {...}
    *       }
    *  }
    **/
    private void gravarResultado() {
        String resultado = validaResultado();
        fireRef.child("usuarios").child(fireUser.getUid()).child("direitoide").setValue(percDiscordoTotalmente);
        fireRef.child("usuarios").child(fireUser.getUid()).child("centroide").setValue(percConcordoPouco);
        fireRef.child("usuarios").child(fireUser.getUid()).child("esquerdoide").setValue(percConcordoTotal);
        fireRef.child("usuarios").child(fireUser.getUid()).child("resultado").setValue(resultado);
    }

    private void selectFireResultado() {

        fireRef.child("usuarios").child(fireUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String resultado = "";
                    try {
                        percDiscordoTotalmente = Float.parseFloat(dataSnapshot.child("direitoide").getValue().toString());
                        percConcordoPouco = Float.parseFloat(dataSnapshot.child("centroide").getValue().toString());
                        percConcordoTotal = Float.parseFloat(dataSnapshot.child("esquerdoide").getValue().toString());
                        resultado = dataSnapshot.child("resultado").getValue().toString();
                    } catch (Exception e) {}
                    tvDireitoide.setText("Direitoide: " + (int) percDiscordoTotalmente + "%");
                    tvCentroide.setText("Centroide: " + (int) percConcordoPouco + "%");
                    tvEsquerdoide.setText("Esquerdoide: " + (int) percConcordoTotal + "%");
                    tvFinalResult.setText("Resultado: " + resultado);

                } else
                    tvFinalResult.setText("Você ainda não fez nenhum teste!");

                popularPoliticalChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Valida o resultado de acordo com as regras de negócio da Lucianna
    private String validaResultado() {
        if (this.pontosConcordoUmPouco >= this.pontosConcordoTotalmente + this.pontosDiscordoTotalmente)
            return "CENTROLOIDE";
        else if (this.pontosConcordoTotalmente >= this.pontosConcordoUmPouco + this.pontosDiscordoTotalmente)
            return "ESQUERDOLOIDE";

        return "DIREITOLOIDE";
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
