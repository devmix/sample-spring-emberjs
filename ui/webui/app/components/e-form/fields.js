import Ember from 'ember';
import AutoRegisterMixin from 'webui/mixins/components/autoregister'
import StyleableMixin from 'webui/mixins/components/styleable';

const EMPTY_PROMISE = {
  resolve: () => {
  },
  reject: () => {
  }
};

export default Ember.Component.extend(AutoRegisterMixin, StyleableMixin, {

  classNames: ['e-form-fields', 'ui small form'],
  classNameBindings: ['valid::error'],

  fields: {},
  valid: true,
  errors: [],
  unsavedPromise: EMPTY_PROMISE,

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
        valid &= fields[id].validate(errors);
      }
    }

    valid &= this.validationCustom(errors);

    this.setProperties({errors, valid});

    return valid;
  },

  cancel() {
    return this.get('model').get('hasDirtyAttributes') ? new Promise((resolve, reject) => {
      this.get('unsavedDialog').show({
        title: 'Confirm',
        message: 'You have unsaved changes. Do you want to leave and discard your changes?',
        buttons: [
          {text: 'Save & Close', action: 'save-changes', class: 'primary'},
          {text: 'Discard', action: 'discard-changes'},
          {text: 'Cancel', action: 'discard-cancel'},
        ]
      });
      this.set('unsavedPromise', {resolve, reject});
    }) : Promise.resolve();
  },

  actions: {
    registerField(field, register) {
      if (register) {
        this.get('fields')[field.get('elementId')] = field;
      } else {
        delete this.get('fields')[field.get('elementId')];
      }
    },

    onUnsavedDialogAction(action) {
      const model = this.get('model');
      const promise = this.get('unsavedPromise');
      if ('save-changes' === action) {
        this.commit().then(promise.resolve, promise.reject);
      } else if ('discard-changes' === action) {
        if (!model.get('isNew')) {
          model.rollbackAttributes();
        }
        promise.resolve();
      }
      this.set('unsavedPromise', EMPTY_PROMISE);
    }
  }
});
