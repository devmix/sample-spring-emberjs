import Ember from 'ember';

const L = Ember.Object.extend({
  link: null,
  name: null,
  menu: null,
  active: false
});

export default Ember.Controller.extend({

  menu: [
    L.create({name: 'Storage', route: 'storage', menu: [
      L.create({name: 'Books', route: 'storage.books.book', menu: [
        L.create({name: 'Browse', route: 'storage.books.book'}),
        L.create({name: 'Summary'}),
        L.create({name: 'Import'}),
        L.create({name: 'Export'}),
        L.create({name: 'Dictionary', menu: [
          L.create({name: 'Author', route: 'storage.books.author'}),
          L.create({name: 'Genre'}),
          L.create({name: 'Publisher'})
        ]}),
      ]}),
      L.create({name: 'Software'})
    ]}),
    L.create({name: 'Administration', menu: [
      L.create({name: 'Swagger API', link: 'http://localhost:8080/swagger-ui.html'}),
    ]})
  ]
});
