import DS from 'ember-data';

export default DS.Model.extend({
  entities: DS.attr('string'),
  state: DS.attr('string'),
  startDate: DS.attr('date'),
  finishDate: DS.attr('date'),
  percent: DS.attr('number'),
  errors: DS.attr('string'),
});
