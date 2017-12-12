import DS from 'ember-data';

export default DS.Model.extend({
  title: DS.attr('string'),
  language: DS.attr('string'),
  publishDate: DS.attr('date'),
  genre: DS.attr('string'),
  isnb13: DS.attr('string'),
  description: DS.attr('string'),
  authors: DS.hasMany('books-author')
});
