import UiModal from 'semantic-ui-ember/components/ui-modal';
import AutoRegisterMixin from 'webui/mixins/components/autoregister';

export default UiModal.extend(AutoRegisterMixin, {

  classNameBindings: ['fullscreen:fullscreen', 'size'],

  title: undefined,
  message: undefined,
  btnAccept: 'Accept',
  btnCancel: 'Cancel',
  autoClose: true,
  fullscreen: false,
  size: undefined, // mini, tiny, small, large

  _visible: false,
  _title: undefined,
  _btnAccept: undefined,
  _btnCancel: undefined,
  _data: undefined,

  show(options = {}) {
    this.setProperties({
      _title: options.title || this.get('title'),
      _message: options.message || this.get('message'),
      _btnAccept: options.btnAccept || this.get('btnAccept'),
      _btnCancel: options.btnCancel || this.get('btnCancel'),
      _data: options.data
    });

    this.set('_visible', true);
    this.execute('show');
  },

  hide() {
    this.execute('hide');
    this.set('_visible', false);
  },

  invokeAction(name) {
    this.sendAction('action', name, this.get('_data'));

    if (this.get('autoClose')) {
      this.hide();
    }
  },

  actions: {
    'e-dialog-modal-accept': function () {
      this.invokeAction('e-dialog-modal-accept');
    },

    'e-dialog-modal-cancel': function () {
      this.invokeAction('e-dialog-modal-cancel');
    }
  }
});
