import Ember from 'ember';
import AutoRegisterMixin from '../../mixins/components/autoregister'

export default Ember.Component.extend(AutoRegisterMixin, {

  classNames: ['component-e-form-fields'],

  fields: {},
  valid: true,
  errors: [],

  commit() {
    if (this.validate()) {
      return this.get('model').save();
    }
    return Promise.reject();
  },

  invalid: Ember.computed('valid', function () {
    return !this.get('valid');
  }),

  validationCustom(errors) {
    return true;
  },

  validationReset() {
    this.set('valid', true);
    this.set('errors', []);
  },

  /**
   * @public
   * @returns {boolean}
   */
  validate() {
    let valid = true, errors = [], fields = this.get('fields');

    for (let id in fields) {
      if (fields.hasOwnProperty(id)) {
        valid &= fields[id].validate();
      }
    }

    valid &= this.validationCustom(errors);

    this.set('errors', errors);
    this.set('valid', valid);

    return valid;
  },

  cancel(cancelledCallback) {
    const model = this.get('model');
    // TODO SG confirm dialog
    if (!model.get('isNew')) {
      model.rollbackAttributes();
    }
    return Promise.resolve();
  },

  actions: {
    registerField(field, register) {
      if (register) {
        this.get('fields')[field.get('elementId')] = field;
      } else {
        delete this.get('fields')[field.get('elementId')];
      }
    }
  }
});
