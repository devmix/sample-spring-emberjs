import Ember from 'ember';

export default Ember.Mixin.create({

  __modalFrameDialogs: {},

  __dialogModalFrameRegister(dialog) {
    this.get('__modalFrameDialogs')[dialog.get('id')] = dialog;
  },

  __dialogModalFrameUnregister(dialog) {
    delete this.get('__modalFrameDialogs')[dialog.get('id')];
  },

  showModalFrameDialog(id, data) {
    const dialog = this.get('__modalFrameDialogs')[id];
    if (dialog) {
      return dialog.show(data);
    }
    return Promise.reject();
  }
});
