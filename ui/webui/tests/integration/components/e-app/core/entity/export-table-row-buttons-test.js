import {moduleForComponent, test} from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-app/core/entity/export-table-row-buttons', 'Integration | Component | e app/core/entity/export table row buttons', {
  integration: true
});

test('it renders', function (assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-app/core/entity/export-table-row-buttons}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-app/core/entity/export-table-row-buttons}}
      template block text
    {{/e-app/core/entity/export-table-row-buttons}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
