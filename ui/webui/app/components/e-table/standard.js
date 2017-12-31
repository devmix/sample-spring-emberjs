import Ember from 'ember';
import PageableMixin from 'webui/mixins/components/e-paging/pageable';
import SearchableMixin from 'webui/mixins/components/e-searching/searchable';
import StyleableMixin from 'webui/mixins/components/styleable';
import AutoRegisterMixin from 'webui/mixins/components/autoregister';

const {computed, on} = Ember;

export default Ember.Component.extend(StyleableMixin, PageableMixin, SearchableMixin, AutoRegisterMixin, {

  classNames: ['e-table', 'e-table-standard'],

  // properties
  attributesConfig: [],
  footerConfig: {},
  items: [],
  modelName: undefined,
  type: 'table',
  gridCellFrame: '',
  persistent: undefined,
  staticFilter: undefined,

  //
  table: undefined,
  store: Ember.inject.service(),
  persistence: Ember.inject.service('core/config'),

  /** @override */
  init() {
    this._super(...arguments);
    this.restoreState();
  },

  computedItems: computed('items', 'modelName', 'filter', function () {
    const modelName = this.get('modelName');
    if (modelName) {
      const filter = this.get('filter');
      this.persistState(this.get('persistent'), filter);
      return this.watchPaging(this.get('store').query(modelName, filter));
    }
    return this.get('items');
  }),

  filter: computed('search', 'page', 'pageSize', 'sort', function () {
    return Object.assign({}, this.get('staticFilter') || {}, {
      page: this.get('page'),
      pageSize: this.get('pageSize'),
      search: this.get('search'),
      sort: this.get('sort')
    });
  }),

  isTable: computed('type', function () {
    return this.get('type') === 'table';
  }),

  isGrid: computed('type', function () {
    return this.get('type') === 'grid';
  }),

  refresh() {
    this.notifyPropertyChange('filter');
  },

  persistState(key, data) {
    key && this.get('persistence').set(key, data);
  },

  restoreState() {
    const key = this.get('persistent');
    if (key) {
      const data = this.get('persistence').get(key);
      if (data) {
        this.setProperties({
          page: data.page <= 0 ? 0 : data.page,
          pageSize: data.pageSize,
          search: data.search,
          sort: data.sort
        })
      }
    }
  },

  invokeAction(action, data) {
    this.sendAction('onCustomAction', action, data);
  },

  registerTableComponent(table) {
    this.set('table', table);
  },

  unregisterTableComponent() {
    this.set('table', undefined);
  },

  _onRefreshEvent: on('refresh', function () {
    this.refresh();
  })
});
