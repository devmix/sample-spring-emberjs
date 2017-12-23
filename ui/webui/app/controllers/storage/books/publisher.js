import Ember from 'ember';

export default Ember.Controller.extend({
  columns: [
    {title: 'Name', id: 'name', sortable: true},
    {view: 'e-app/commons/table/cell-entity-buttons', width: '70px'}
  ]
});
