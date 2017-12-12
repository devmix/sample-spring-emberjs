import Ember from 'ember';
import AbstractField from './abstract-field';

export default AbstractField.extend({

  modelName: undefined,
  multiple: false,
  searchField: 'name',
  options: undefined,
  maxOptions: 1000,

  store: Ember.inject.service(),

  internalOptions: Ember.computed(function () {
    const models = this.get('options');
    if (models) {
      return models;
    }

    const modelName = this.get('modelName');
    if (modelName) {
      return this.get('store').query(modelName, {page: 0, size: this.get('maxOptions')});
    }

    return [];
  }),

});
