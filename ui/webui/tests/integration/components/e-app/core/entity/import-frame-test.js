import {moduleForComponent, test} from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-app/core/entity/import-frame', 'Integration | Component | e app/core/entity/import frame', {
  integration: true
});

test('it renders', function (assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-app/core/entity/import-frame}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-app/core/entity/import-frame}}
      template block text
    {{/e-app/core/entity/import-frame}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
