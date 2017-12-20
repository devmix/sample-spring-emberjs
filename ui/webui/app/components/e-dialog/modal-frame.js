import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'div',
  classNames: ['modal', 'fade'],
  attributeBindings: ['id', 'tabindex', 'role'],

  visible: false,
  bodyFrame: undefined,
  bodyText: undefined,
  frame: undefined,
  btnAccept: 'Save',
  btnCancel: 'Cancel',
  width: '',

  tabindex: -1,
  role: 'dialog',

  didInsertElement() {
    this._super(...arguments);

    const owner = this.get('owner');
    if (owner && owner.__dialogModalFrameRegister) {
      owner.__dialogModalFrameRegister(this);
    }
  },

  willDestroyElement() {
    const owner = this.get('owner');
    if (owner && owner.__dialogModalFrameUnregister) {
      owner.__dialogModalFrameUnregister(this);
    }

    this._super(...arguments);
  },

  show(data) {
    this.setProperties({
      visible: true,
      params: data
    });

    this.$().modal('show');

    return new Promise((resolve, reject) => {
      this.callbackResolve = resolve;
      this.callbackReject = reject;
    });
  },

  invokeAction(name) {
    const frame = this.get('frame');
    if (frame) {
      this.get('frame')
        .onDialogAction(name)
        .then((action) => {
          this.$().modal('hide');
          if ('cancel' == name) {
            this.callbackReject && this.callbackReject(name)
          } else {
            this.callbackResolve && this.callbackResolve(name);
          }
        }, () => this.callbackReject && this.callbackReject(name));
    } else {
      this.$().modal('hide');
      if ('cancel' == name) {
        this.callbackReject && this.callbackReject(name);
      } else {
        this.callbackResolve && this.callbackResolve(name);
      }
    }
  },

  actions: {
    registerFrame(frame) {
      this.set('frame', frame);
    },

    unregisterFrame() {
      this.set('frame', undefined);
    },

    cancel() {
      this.invokeAction('cancel');
    },

    accept() {
      this.invokeAction('accept');
    }
  }
});
