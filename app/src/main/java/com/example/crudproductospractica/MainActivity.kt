package com.example.crudproductospractica

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudproductospractica.adapters.ArticuloAdapter
import com.example.crudproductospractica.databinding.ActivityMainBinding
import com.example.crudproductospractica.models.Articulo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    private val adapter = ArticuloAdapter(
        mutableListOf(),
        onEdit = { articulo -> editarArticulo(articulo) },
        onDelete = { articulo -> borrarArticulo(articulo) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("tienda")
        setRecyclerView()
        setListeners()
        cargarArticulos()
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarArticulos() {
        database.orderByChild("nombre").get().addOnSuccessListener { snapshot ->
            val articulos = mutableListOf<Articulo>()
            snapshot.children.forEach { data ->
                data.getValue(Articulo::class.java)?.let { articulos.add(it) }
            }
            binding.emptyView.visibility = if (articulos.isEmpty()) View.VISIBLE else View.GONE
            adapter.updateList(articulos)
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar los artículos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarArticulo(articulo: Articulo) {
        database.child(articulo.nombre).removeValue().addOnSuccessListener {
            cargarArticulos()
            Toast.makeText(this, "Artículo eliminado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar el artículo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editarArticulo(articulo: Articulo) {
        val intent = Intent(this, AddActivity::class.java).apply {
            putExtra("articulo", articulo)
        }
        startActivity(intent)
    }

    // ------------------------- Menú lateral -------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> cerrarSesion()
            R.id.action_exit -> salirApp()
            R.id.action_delete_all -> confirmarBorrado()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun salirApp() {
        finishAffinity() // Cierra todas las actividades
    }

    private fun confirmarBorrado() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar borrado")
            .setMessage("¿Estás seguro de que quieres borrar todos los artículos?")
            .setPositiveButton("Sí") { _, _ -> borrarTodosLosArticulos() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun borrarTodosLosArticulos() {
        database.removeValue().addOnSuccessListener {
            cargarArticulos()
            Toast.makeText(this, "Todos los artículos han sido eliminados", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar los artículos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarArticulos()
    }
}
