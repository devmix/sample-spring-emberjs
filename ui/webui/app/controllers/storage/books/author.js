import Ember from 'ember';

export default Ember.Controller.extend({
  columns: [
    {title: 'First Name', id: 'firstName', sortable: true},
    {title: 'Middle Name', id: 'middleName', sortable: true},
    {title: 'Last Name', id: 'lastName', sortable: true},
    {view: 'e-app/commons/table/cell-entity-buttons', width: '70px'},
  ]
});
