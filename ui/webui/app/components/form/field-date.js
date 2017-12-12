import Ember from 'ember';
import AbstractField from './abstract-field';

export default AbstractField.extend({

  actions: {
    onSelectDate(value) {
      this.set('currentValue', value);
    }
  }
});
